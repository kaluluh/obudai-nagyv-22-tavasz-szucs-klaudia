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

    public UserDTO authenticate(Credentials credentials) {
        UserDTO user = this.dataStore.getUsers()
                .stream().filter(p ->
                        p.getLoginName().equals(credentials.getUserName()) &&
                                p.getPassword().equals(credentials.getPassword()))
                .findFirst()
                .orElse(null);
        return user;
    }

    public PlayerDTO findUserData(UserDTO user) {
        PlayerDTO player = new PlayerDTO();
        player.setRoles(user.getRoles());
        player.setGameIds(user.getGames());
        player.setId(user.getId());
        player.setName(user.getName());

        this.dataStore.getGames().forEach( g -> {
            player.getGameIds().forEach( u -> {
                if (g.getId() == u) {
                    player.getGames().add(g);
                }
            });
        });

        this.dataStore.getGroups().forEach( g -> {
            g.getMembers().forEach( m -> {
                if (m == player.getId()){
                    player.setGroupName(g.getName());
                    player.setGroupId(g.getId());
                }
            });
        });

        List<JoinRequestDTO> joinRequestList = this.dataStore.getJoinRequests().stream().filter(j ->
                j.getGroupId() ==  player.getGroupId() && j.getState() == JoinRequestState.REQUESTED)
                .collect(Collectors.toList());

        if (joinRequestList != null){
            joinRequestList.forEach( j -> {
                this.dataStore.getUsers().forEach(p -> {
                    if (j.getUserId() == p.getId()) {
                        player.addPlayerName(p.getName());
                        player.addPlayerJoinRequest(p);
                    }
                });
            });
        }
        this.dataStore.setPlayer(player);
        return player;
    }

    public List<GameDTO> getGameList() {
        return this.dataStore.getGames();
    }

    public List<GameDTO> getAllOptionalGames() {
        List<GameDTO> optionalGames = new ArrayList<>(this.dataStore.getGames());
        if (this.dataStore.getPlayer().getGames() != null) {
            optionalGames.removeAll(this.dataStore.getPlayer().getGames());
        }
        return optionalGames;
    }

    public boolean addingNewGame(int selectedNumber) {
        int size = getAllOptionalGames().size();
        boolean isSuccess = false;
         if (selectedNumber > size) {
         } else {
             GameDTO selectedGame = getAllOptionalGames().get(selectedNumber - 1);
             this.dataStore.getPlayer().getGameIds().add(selectedGame.getId());
             this.dataStore.getGames().add(selectedGame);
             isSuccess = true;
         }
         return isSuccess;
    }

    public List<GroupDTO> getOptionalGroups() {
        List<GroupDTO> optionalGroups = new ArrayList<>(this.dataStore.getGroups());
        GroupDTO existedGroup = optionalGroups
                .stream().filter(g -> g.getId() == this.dataStore.getPlayer().getGroupId())
                .findFirst()
                .orElse(null);
        if (existedGroup != null) {
            optionalGroups.remove(existedGroup);
        }
        return optionalGroups;
    }

    public boolean addingNewJoinRequest(int selectedNumber) {
        int size = getOptionalGroups().size();
        boolean isSuccess = false;
        if (selectedNumber > size) {

        } else {
            GroupDTO selectedGroup = getOptionalGroups().get(selectedNumber - 1);
            this.dataStore.getJoinRequests()
                    .add(new JoinRequestDTO(this.dataStore.getPlayer().getId(),
                            selectedGroup.getId(),
                            JoinRequestState.REQUESTED));
            isSuccess = true;
        }
        return isSuccess;
    }

    public List<UserDTO> getJoinRequests () {
        return this.dataStore.getPlayer().getJoinRequests();
    }

    public JoinRequestState proccessSelectedJoinRequests(String selectedJoinRequest) {
        JoinRequestState state = JoinRequestState.REQUESTED;
        int index = 0;
        if (selectedJoinRequest.contains("A") || selectedJoinRequest.contains("R")) {
            index =  Integer.parseInt(selectedJoinRequest.substring(0,selectedJoinRequest.length() - 1));
        } else {
            index =  Integer.parseInt(selectedJoinRequest);
        }
        if ( index > getJoinRequests().size()) {
        } else {
            if(selectedJoinRequest.contains("A")) {
                UserDTO selectedPlayer  = getJoinRequests().get(index - 1);
                this.dataStore.getJoinRequests().add(new JoinRequestDTO(selectedPlayer.getId(),
                        this.dataStore.getPlayer().getGroupId(),
                        JoinRequestState.ACCEPTED));
                state = JoinRequestState.ACCEPTED;
            } else if (selectedJoinRequest.contains("R")) {
                UserDTO selectedPlayer  = getJoinRequests().get(index - 1);
                this.dataStore.getJoinRequests().add(new JoinRequestDTO(selectedPlayer.getId(),
                        this.dataStore.getPlayer().getGroupId(),
                        JoinRequestState.REJECTED));
                state = JoinRequestState.REJECTED;
            }
        }
        return state;
    }

    public void quitApplication() {
        this.dataStore.writeResultToJSON();
    }

    public boolean addNewGame(GameFormDTO gameFormDTO) {
        boolean isSucces = false;
        List<Category> categoriesList = new ArrayList<>();
        if (gameFormDTO.getCategories().contains(",")) {
            String[] categoriesArray = gameFormDTO.getCategories().split(",");
            for (String category: categoriesArray) {
                categoriesList.add(Category.valueOf(category));
            }
        } else {
            categoriesList.add(Category.valueOf(gameFormDTO.getCategories()));
        }

        this.dataStore.getGames().add(
                new GameDTO(gameFormDTO.getId(),gameFormDTO.getName(),gameFormDTO.getDescription(),gameFormDTO.getMinimumAge(),categoriesList,
                new LimitsDTO(gameFormDTO.getMin(),gameFormDTO.getMax()),
                new Limits(gameFormDTO.getFrom(),gameFormDTO.getTo())));

        if (this.dataStore.getGames().stream().anyMatch(g->g.getId() == gameFormDTO.getId())) {
            isSucces = true;
        }
        return isSucces;
    }

}
