package com.example.dto;

import com.example.domain.JoinRequestState;
import lombok.Data;

@Data
public class JoinRequestDTO {
    private Long userId;
    private Long groupId;
    private JoinRequestState state;
}
