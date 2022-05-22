package com.example;

import com.example.domain.*;
import com.example.dto.EventDTO;
import com.example.dto.JoinRequestsDTO;
import com.example.entity.Event;
import com.example.entity.Game;
import com.example.entity.Group;
import com.example.entity.JoinRequest;
import com.example.entity.JoinRequestId;
import com.example.entity.Player;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final EventRepository eventRepository;


    public void findCurrentPlayerAndGroup () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        MetaData.currentPlayer = playerRepository.findByLoginNameAndPassword(userDetails.getUsername(), userDetails.getPassword());
        MetaData.currentPlayerGroup = this.getGroup(MetaData.currentPlayer);
    }

    public Group getGroupById (String groupId) {
        Group group = groupRepository.findById(Long.parseLong(groupId)).get();
        return group;
    }

    public Group getGroup(Player player) {
        Group group = new Group();
        if (player.getRoles().contains(Role.GROUP_ADMIN)) {
            group = getGroupForAdmin(player);
        } else if (player.getRoles().contains(Role.PLAYER)){
            group = getGroupForPlayer(player);
        }
        return group;
    }

    public Group getGroupForAdmin(Player player) {
        Group group = groupRepository.findByAdmin(player);
        // Lazy loaded collections need to be initialized, for this I'm using a toString call
        group.toString();
        MetaData.currentPlayerGroup = group;
        return group;
    }

    public Group getGroupForPlayer(Player player) {
        Group group = groupRepository.findByPlayer(player);
        // Lazy loaded collections need to be initialized, for this I'm using a toString call
        if (group != null) {
            group.toString();
            MetaData.currentPlayerGroup = group;
        }
        return group;
    }

    public List<Game> getGameList() {
        List<Game> games = (ArrayList<Game>)gameRepository.findAll();
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

    public void addNewGame(String gameId) {
        try {
            Game game = gameRepository.findById(Long.parseLong(gameId)).get();
            MetaData.currentPlayer.getGames().add(game);
            playerRepository.save(MetaData.currentPlayer);
        } catch (Exception e) {
            log.error("Error saving game", e);
        }
    }

    public List<Group> getJoinableGroups() {
        List<Group> groups = (List<Group>)groupRepository.findAll();
        groups.remove(MetaData.currentPlayerGroup);
        return groups;
    }

    public boolean addJoinRequest(int selectedNumber) {
        List<Group> groups = getJoinableGroups();
        boolean success = false;
        if (selectedNumber <= groups.size()) {
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

    public List<JoinRequest> getJoinRequests() {
        return MetaData.currentPlayerGroup.getJoinRequests();
    }

    public List<String> getJoinRequestPlayerNames() {
        List<Player> players = (List<Player>)playerRepository.findAllById(
                MetaData.currentPlayerGroup.getJoinRequests().stream()
                .map(r -> r.getJoinRequestId().getPlayerId()).collect(Collectors.toList()));
        return players.stream().map(p -> p.getName()).collect(Collectors.toList());
    }

    public void processJoinRequest(JoinRequest joinRequest) {
        JoinRequest oldJoinRequest = getJoinRequests().stream()
                .filter(j -> j.getJoinRequestId().getGroupId() == joinRequest.getJoinRequestId().getGroupId()
                        && j.getJoinRequestId().getPlayerId() == joinRequest.getJoinRequestId().getPlayerId())
                .findFirst()
                .get();

        try {
            oldJoinRequest.setJoinRequestState(joinRequest.getJoinRequestState());
            joinRequestRepository.delete(oldJoinRequest);
            joinRequestRepository.save(joinRequest);
            if (joinRequest.getJoinRequestState() == JoinRequestState.ACCEPTED) {
                Player newPlayer = playerRepository.findById(joinRequest.getJoinRequestId().getPlayerId()).orElse(null);
                MetaData.currentPlayerGroup.getMembers().add(newPlayer);
                groupRepository.save(MetaData.currentPlayerGroup);
            }
        } catch (Exception e) {
            log.error("Error handling join request", e);
        }
    }

    public void quitApplication() {
        System.exit(0);
    }

    public void attendGroup(Long playerId,Long groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        JoinRequest joinRequest = new JoinRequest(new JoinRequestId(playerId,groupId),JoinRequestState.REQUESTED);
        joinRequestRepository.save(joinRequest);
        group.getJoinRequests().add(joinRequest);
        groupRepository.save(group);
    }

    public void attendEvent(Long playerId,Long eventId) {
        Player player = playerRepository
                .findById(playerId)
                .orElse(null);
        Event event = eventRepository
                .findById(eventId)
                .orElse(null);

        event.getParticipants().add(player);
        eventRepository.save(event);
    }

    public List<JoinRequestsDTO> getJoinRequestsPlayerNames(List<JoinRequest> joinRequests) {
        List<JoinRequestsDTO> joinRequestsDTOS = new ArrayList<>();
        joinRequests = joinRequests
                .stream()
                .filter(j -> j.getJoinRequestState() == JoinRequestState.REQUESTED)
                .collect(Collectors.toList());

        for (JoinRequest joinRequest: joinRequests) {
            Player player = playerRepository
                    .findById(joinRequest.getJoinRequestId().getPlayerId())
                    .orElse(null);
            joinRequestsDTOS.add(new JoinRequestsDTO(player.getName(), player.getId(),null,false));
        }
        return  joinRequestsDTOS;
    }

    public List<Event> getEvents (Long groupId) {
        Group group = groupRepository
                .findById(groupId)
                .orElse(null);
        return group.getEvents();
    }

    public void saveEvent(Event event) {
        Long lastEventId = eventRepository.findTopByOrderByIdDesc().getId() + 1;
        event.setId(lastEventId);
        eventRepository.save(event);

//        MetaData.currentPlayerGroup.getEvents().add(event);
//        groupRepository.save(MetaData.currentPlayerGroup);
    }

    public void saveGame(Game game) {
        Long lastGameId = gameRepository.findTopByOrderByIdDesc().getId() + 1;
        game.setId(lastGameId);
        gameRepository.save(game);
    }
}
