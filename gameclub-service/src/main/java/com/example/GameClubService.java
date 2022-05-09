package com.example;

import com.example.domain.Category;
import com.example.domain.Credentials;
import com.example.domain.GameForm;
import com.example.domain.JoinRequestState;
import com.example.entity.*;
import com.example.entity.Group;
import com.example.entity.JoinRequestId;
import com.example.entity.Player;
import com.example.repository.GameRepository;
import com.example.repository.GroupRepository;
import com.example.repository.JoinRequestRepository;
import com.example.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GameClubService {

    private final GameRepository gameRepository;
    private final GroupRepository groupRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final PlayerRepository playerRepository;

    public Player authenticate(Credentials credentials) {
        Player player = playerRepository.findByLoginNameAndPassword(credentials.getUserName(), credentials.getPassword());
        // Lazy loaded collections need to be initialized, for this I'm using a toString call
        if (player != null) {
            player.getGames().toString();
            player.getRoles().toString();
            MetaData.currentPlayer = player;
        }
        return player;
    }

    public Group getGroupForAdmin(Player player) {
        Group group = groupRepository.findByAdmin(player);
        // Lazy loaded collections need to be initialized, for this I'm using a toString call
        group.toString();
        MetaData.currentPlayerGroup = group;
        return group;
    }

    public com.example.entity.Group getGroupForPlayer(com.example.entity.Player player) {
        Group group = groupRepository.findByPlayer(player);
        // Lazy loaded collections need to be initialized, for this I'm using a toString call
        group.toString();
        MetaData.currentPlayerGroup = group;
        return group;
    }

    public List<Game> getGameList() {
        List<Game> games = (ArrayList<com.example.entity.Game>)gameRepository.findAll();
        // Lazy loaded collection need to be initialized, for this I'm using a toString call
        games.toString();
        return games;
    }

    public List<Game> getGamesNotOwnedByPlayer() {
        List<Game> games = getGameList();
        games.removeAll(MetaData.currentPlayer.getGames());
        return games;
    }

    public List<Game> getAllOptionalGames() {
        return getGamesNotOwnedByPlayer();
    }

    public boolean addNewGame(int selectedNumber) {
        List<Game> optionalGames = getGamesNotOwnedByPlayer();
        boolean success = false;

        if (!optionalGames.isEmpty()) {
            if (selectedNumber < 0 && selectedNumber <= optionalGames.size()) {
            } else {
                Game selectedGame = optionalGames.get(selectedNumber - 1);
                MetaData.currentPlayer.getGames().add(selectedGame);
                try {
                    playerRepository.save(MetaData.currentPlayer);
                    success = true;
                } catch (Exception e) {
                    log.error("Error saving game", e);
                }
            }
        } else {
            success = true;
        }
        return success;
    }

    public List<Group> getJoinableGroups() {
        List<Group> groups = (List<Group>)groupRepository.findAll();
        groups.remove(MetaData.currentPlayerGroup);
        return groups;
    }

    public boolean addJoinRequest(int selectedNumber) {
        List<Group> groups = getJoinableGroups();
        boolean success = false;
        if (selectedNumber < groups.size()) {
            Group selectedGroup = groups.get(selectedNumber - 1);
            JoinRequestId joinRequestId = new JoinRequestId(MetaData.currentPlayer.getId(), selectedGroup.getId());
            try {
                joinRequestRepository.save(new JoinRequest(joinRequestId, JoinRequestState.REQUESTED));
                success = true;
            } catch (Exception e) {
                log.error("Error saving join request", e);
            }
        }
        return success;
    }

    public List<com.example.entity.JoinRequest> getJoinRequests() {
        return MetaData.currentPlayerGroup.getJoinRequests();
    }

    public List<String> getJoinRequestPlayerNames() {
        List<Player> players = (List<Player>)playerRepository.findAllById(
                MetaData.currentPlayerGroup.getJoinRequests().stream()
                .map(r -> r.getJoinRequestId().getPlayerId()).collect(Collectors.toList()));
        return players.stream().map(p -> p.getName()).collect(Collectors.toList());
    }

    public JoinRequestState processSelectedJoinRequests(String joinRequestId) {
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
            try {
                joinRequest.setJoinRequestState(state);
                joinRequestRepository.save(joinRequest);
                MetaData.currentPlayerGroup.getJoinRequests().remove(joinRequest);
            } catch (Exception e) {
                log.error("Error handling join request", e);
            }
        }
        return state;
    }

    public void quitApplication() {
        System.exit(0);
    }

    public boolean addNewGame(GameForm gameForm) {
        boolean success = false;
        List<Category> categories = new ArrayList<>();
        if (gameForm.getCategories().contains(",")) {
            String[] categoriesArray = gameForm.getCategories().split(",");
            for (String category: categoriesArray) {
                categories.add(Category.valueOf(category));
            }
        } else {
            categories.add(Category.valueOf(gameForm.getCategories()));
        }
        Game game = Game.builder()
                .name(gameForm.getName())
                .description(gameForm.getDescription())
                .minimumAge(gameForm.getMinimumAge())
                .categories(categories)
                .playTime(new com.example.entity.Limits(gameForm.getMin(), gameForm.getMax()))
                .numberOfPlayers(new com.example.entity.Limits(gameForm.getFrom(), gameForm.getTo()))
                .build();
        try {
            gameRepository.save(game);
            success = true;
        } catch (Exception e) {
            log.error("Error saving game", e);
        }

        return success;
    }

}
