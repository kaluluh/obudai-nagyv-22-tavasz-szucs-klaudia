package com.example.dto;

import com.example.domain.JoinRequestState;
import com.example.entity.Event;
import com.example.entity.JoinRequest;
import com.example.entity.JoinRequestId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestsDTO implements TransformableToDomainObject<JoinRequest> {

    private String playerName;
    private Long playerId;
    private Long groupId;
    private boolean isAccepted;

    @Override
    public JoinRequest toDomainObject() {
        JoinRequestState state = isAccepted != false ? JoinRequestState.ACCEPTED : JoinRequestState.REJECTED;
        return JoinRequest.builder()
                .joinRequestId(new JoinRequestId(playerId,groupId))
                .joinRequestState(state)
                .build();
    }
}
