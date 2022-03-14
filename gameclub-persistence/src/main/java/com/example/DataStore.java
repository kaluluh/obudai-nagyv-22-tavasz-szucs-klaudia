package com.example;

import com.example.dto.GameDTO;
import com.example.dto.GroupDTO;
import com.example.dto.JoinRequestDTO;
import com.example.dto.PlayerDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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

}
