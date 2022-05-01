package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinRequest {

    private Long playerId;
    private String playerName;
    private JoinRequestState joinRequestState;
}
