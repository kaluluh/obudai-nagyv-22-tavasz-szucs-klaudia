package com.example;

import com.example.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class DataStore {
    private static final String PLAYERS_FILE_PATH = "gameclub-persistence/src/main/resources/users.json";
    private static final String GAMES_FILE_PATH = "gameclub-persistence/src/main/resources/games.json";
    private static final String GROUPS_FILE_PATH = "gameclub-persistence/src/main/resources/groups.json";
    private static final String JOINREQUESTS_FILE_PATH = "gameclub-persistence/src/main/resources/joinRequests.json";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    public List<PlayerDTO> players;

    @Getter
    private List<GameDTO> games;

    @Getter
    private List<GroupDTO> groups;

    @Getter
    private List<JoinRequestDTO> joinRequests;

    @Getter
    @Setter
    private CredentialsDTO credentialsDTO;

    @Getter
    @Setter
    private UserDataDTO userDataDTO;

    @PostConstruct
    public void loadData() {
        players = readPlayers();
        games = readGames();
        groups = readGroups();
        joinRequests = readJoinRequests();
    }

    public static String readFileAsString(String file) {
        String result = null;
        try {
            result = new String(Files.readAllBytes(Paths.get(file)));
        } catch (Exception e) {
            log.error("Error parsing file: {}", file, e);
        }
        return result;
    }

    public List<PlayerDTO> readPlayers() {
        List<PlayerDTO> players = new ArrayList<>();
        String json = readFileAsString(PLAYERS_FILE_PATH);
        if (json != null) {
            try {
                players = objectMapper.readValue(json,new TypeReference<List<PlayerDTO>>(){});
            } catch (Exception e) {
                log.error("Error reading players: {}", json, e);
            }
        }
        return players;
    }

    public List<GameDTO> readGames() {
        List<GameDTO> games = new ArrayList<>();
        String json = readFileAsString(GAMES_FILE_PATH);
        if (json != null) {
            try {
                games = objectMapper.readValue(json,new TypeReference<List<GameDTO>>(){});
            } catch (Exception e) {
                log.error("Error reading players: {}", json, e);
            }
        }
        return games;
    }

    public List<GroupDTO> readGroups() {
        List<GroupDTO> groups = new ArrayList<>();
        String json = readFileAsString(GROUPS_FILE_PATH);
        if (json != null) {
            try {
                groups = objectMapper.readValue(json,new TypeReference<List<GroupDTO>>(){});
            } catch (Exception e) {
                log.error("Error reading players: {}", json, e);
            }
        }
        return groups;
    }

    public List<JoinRequestDTO> readJoinRequests() {
        List<JoinRequestDTO> joinRequests = new ArrayList<>();
        String json = readFileAsString(JOINREQUESTS_FILE_PATH);
        if (json != null) {
            try {
                joinRequests = objectMapper.readValue(json,new TypeReference<List<JoinRequestDTO>>(){});
            } catch (Exception e) {
                log.error("Error reading players: {}", json, e);
            }
        }
        return joinRequests;
    }

    public void writeResultToJSON(List<PlayerDTO> players,List<GameDTO> games, List<GroupDTO> groups, List<JoinRequestDTO> joinRequests) {
        writePlayersToJSON(players);
        writeGamesToJSON(games);
        writeGroupsToJSON(groups);
        writejoinRequestsToJSON(joinRequests);
    }

    private void writePlayersToJSON(List<PlayerDTO> players) {
        if (players != null) {
            try {
                objectMapper.writeValue(new File(PLAYERS_FILE_PATH), players);
            } catch (Exception e ) { log.error("Error writing players...");}
        }
    }

    private void writeGamesToJSON(List<GameDTO> games) {
        if (games != null) {
            try {
                objectMapper.writeValue(new File(GAMES_FILE_PATH), games);
            } catch (Exception e ) { log.error("Error writing games...");}
        }
    }

    private void writeGroupsToJSON(List<GroupDTO> groups) {
        if (groups != null) {
            try {
                objectMapper.writeValue(new File(GROUPS_FILE_PATH), groups);
            } catch (Exception e ) { log.error("Error writing groups...");}
        }
    }

    private void writejoinRequestsToJSON(List<JoinRequestDTO> joinRequests) {
        if (joinRequests != null) {
            try {
                objectMapper.writeValue(new File(JOINREQUESTS_FILE_PATH), joinRequests);
            } catch (Exception e ) { log.error("Error writing join requests...");}
        }
    }


}
