package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Player extends User {

    private GroupInfo groupInfo;
    private List<Game> games;
    private List<JoinRequest> joinRequests;
}
