package com.example.entity;

import com.example.domain.JoinRequestState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "join_requests")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinRequest {

    @EmbeddedId
    private JoinRequestId joinRequestId;
    private JoinRequestState joinRequestState;

}
