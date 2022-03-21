package com.example.dto;

import com.example.domain.JoinRequestState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDTO {
    private Long userId;
    private Long groupId;
    private JoinRequestState state;

}
