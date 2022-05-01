package com.example;

import com.example.domain.*;
import com.example.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameClubService {

    private final DataStore dataStore;

    public User authenticate(Credentials credentials) {
        UserDTO user = this.dataStore.getUsers()
                .stream().filter(p ->
                        p.getLoginName().equals(credentials.getUserName()) &&
                                p.getPassword().equals(credentials.getPassword()))
                .findFirst()
                .orElse(null);
        return user == null ? null : user.toDomainObject();
    }

    private List<Game> getGamesForUser(UserDTO user) {
        return dataStore.getGames().stream()
                .filter(g -> user.getGames().contains(g.getId()))
                .map(GameDTO::toDomainObject)
                .collect(Collectors.toList());
    }

    private GroupInfo getGroupInfoForUser(UserDTO user) {
        GroupDTO group = dataStore.getGroups().stream()
                .filter(g -> g.getMembers().contains(user.getId()))
                .findFirst().orElse(null);
        return group != null ? new GroupInfo(group.getId(), group.getName()) : new GroupInfo(-1L, "N/A");
    }

    private List<JoinRequest> getJoinRequestsForPlayer(Player player) {
        return dataStore.getJoinRequests().stream()
                .filter(r -> r.getGroupId() == player.getGroupInfo().getId() && r.getState() == JoinRequestState.REQUESTED)
                .map(r -> new JoinRequest(player.getId(), player.getName(), r.getState()))
                .collect(Collectors.toList());
    }

    public Player findUserData(User user) {
        Player player = new Player();
        player.setRoles(user.getRoles());
        player.setId(user.getId());
        player.setName(user.getName());

        UserDTO userDTO = dataStore.getUsers().stream()
                .filter(u -> user.getId() == u.getId()).findFirst().get();

        player.setGames(getGamesForUser(userDTO));
        player.setGroupInfo(getGroupInfoForUser(userDTO));

        if (player.getRoles().contains(Role.GROUP_ADMIN)) {
            player.setJoinRequests(getJoinRequestsForPlayer(player));
        }

        this.dataStore.setCurrentPlayer(player);

        return player;
    }

    public List<Game> getGameList() {
        return this.dataStore.getGames().stream()
                .map(GameDTO::toDomainObject)
                .collect(Collectors.toList());
    }

    public List<Game> getAllOptionalGames() {
        List<GameDTO> optionalGames = new ArrayList<>(this.dataStore.getGames());
        if (this.dataStore.getCurrentPlayer().getGames() != null) {
            optionalGames.removeAll(this.dataStore.getCurrentPlayer().getGames());
        }
        return optionalGames.stream()
                .map(GameDTO::toDomainObject)
                .collect(Collectors.toList());
    }

    public boolean addNewGame(int selectedNumber) {
        List<Game> optionalGames = getAllOptionalGames();
        boolean isSuccess = false;
        if (selectedNumber > optionalGames.size()) {
        } else {
            Game selectedGame = getAllOptionalGames().get(selectedNumber - 1);
            this.dataStore.getCurrentPlayer().getGames().add(selectedGame);
            // Szerintem ez nem kell mert csak az aktualis player jatekaihoz kell hozzaadni
//             this.dataStore.addGame(GameDTO.builder()
//                             .id(selectedGame.getId())
//                             .name(selectedGame.getName())
//                             .description(selectedGame.getDescription())
//                             .minimumAge(selectedGame.getMinimumAge())
//                             .categories(selectedGame.getCategories())
//                             .numberOfPlayers(selectedGame.getNumberOfPlayers())
//                             .playTime(new LimitsDTO(selectedGame.getPlayTime().getMin(), selectedGame.getPlayTime().getMax()))
//                             .build());
            isSuccess = true;
        }
        return isSuccess;
    }

    public List<Group> getOptionalGroups() {
        return getOptionalGroupsInternal().stream()
                .map(GroupDTO::toDomainObject)
                .collect(Collectors.toList());
    }

    private List<GroupDTO> getOptionalGroupsInternal() {
        List<GroupDTO> optionalGroups = new ArrayList<>(this.dataStore.getGroups());
        GroupDTO existedGroup = optionalGroups
                .stream().filter(g -> g.getId() == this.dataStore.getCurrentPlayer().getGroupInfo().getId())
                .findFirst()
                .orElse(null);
        if (existedGroup != null) {
            optionalGroups.remove(existedGroup);
        }
        return optionalGroups;
    }

    public boolean addJoinRequest(int selectedNumber) {
        int size = getOptionalGroupsInternal().size();
        boolean isSuccess = false;
        if (selectedNumber < size) {
            GroupDTO selectedGroup = getOptionalGroupsInternal().get(selectedNumber - 1);
            this.dataStore.addJoinRequest(new JoinRequestDTO(this.dataStore.getCurrentPlayer().getId(),
                    selectedGroup.getId(),
                    JoinRequestState.REQUESTED));
            isSuccess = true;
        }
        return isSuccess;
    }

    public List<JoinRequest> getJoinRequests() {
        return this.dataStore.getCurrentPlayer().getJoinRequests();
    }

    public JoinRequestState proccessSelectedJoinRequests(String joinRequestId) {
        JoinRequestState state = JoinRequestState.REQUESTED;
        int index = 0;
        if (joinRequestId.contains("A") || joinRequestId.contains("R")) {
            index =  Integer.parseInt(joinRequestId.substring(0,joinRequestId.length() - 1));
        } else {
            index =  Integer.parseInt(joinRequestId);
        }
        if ( index > getJoinRequests().size()) {
        } else {
            JoinRequest joinRequest = getJoinRequests().get(index - 1);
            if (joinRequestId.contains("A")) {
                state = JoinRequestState.ACCEPTED;
            } else if (joinRequestId.contains("R")) {
                state = JoinRequestState.REJECTED;
            }
            dataStore.addJoinRequest(new JoinRequestDTO(joinRequest.getPlayerId(),
                    this.dataStore.getCurrentPlayer().getGroupInfo().getId(), state));
        }
        return state;
    }

    public void quitApplication() {
        this.dataStore.writeResultToJSON();
    }

    public boolean addNewGame(GameForm gameForm) {
        boolean success = false;
        List<Category> categoriesList = new ArrayList<>();
        if (gameForm.getCategories().contains(",")) {
            String[] categoriesArray = gameForm.getCategories().split(",");
            for (String category: categoriesArray) {
                categoriesList.add(Category.valueOf(category));
            }
        } else {
            categoriesList.add(Category.valueOf(gameForm.getCategories()));
        }

        this.dataStore.addGame(
                new GameDTO(gameForm.getId(), gameForm.getName(), gameForm.getDescription(), gameForm.getMinimumAge(),categoriesList,
                        new LimitsDTO(gameForm.getMin(), gameForm.getMax()),
                        new Limits(gameForm.getFrom(), gameForm.getTo())));

        if (this.dataStore.getGames().stream().anyMatch(g->g.getId() == gameForm.getId())) {
            success = true;
        }
        return success;
    }

}
