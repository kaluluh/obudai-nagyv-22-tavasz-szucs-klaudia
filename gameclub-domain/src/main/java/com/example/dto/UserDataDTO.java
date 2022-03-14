package com.example.dto;

import com.example.domain.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDataDTO {
    private Long id;
    private String name;
    private List<Long> gameIds;
    private List<String> gameNames = new ArrayList<>();
    private Long groupId;
    private String groupName;
    private List<String> joinRequestedGroupName = new ArrayList<>();
    private List<Role> roles;

    public void addPlayerName(String name) {
        this.joinRequestedGroupName.add(name);
    }
    public void addGameName(String name) {
        this.gameNames.add(name);
    }

}
