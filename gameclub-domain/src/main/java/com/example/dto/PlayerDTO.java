package com.example.dto;

import com.example.domain.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PlayerDTO {
    private Long id;
    private String name;
    private List<Long> gameIds;
    private Long groupId;
    private String groupName;
    private List<String> joinRequestedGroupName = new ArrayList<>();
    private List<UserDTO> joinRequests = new ArrayList<>();
    private List<Role> roles;
    private List<GameDTO> games = new ArrayList<>();

    public void addPlayerName(String name) {
        this.joinRequestedGroupName.add(name);
    }
    public List<String> getGameNames() {
        return games.stream().map(GameDTO::getName).collect(Collectors.toList());
    }
    public void addPlayerJoinRequest(UserDTO userDTO) {
        this.joinRequests.add(userDTO);
    }
}
