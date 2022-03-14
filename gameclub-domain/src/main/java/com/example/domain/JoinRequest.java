package com.example.domain;

import lombok.Data;

@Data
public class JoinRequest {

    private Player player;
    private Group group;
    private JoinRequestState joinRequestState;
}
