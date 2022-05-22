package com.example.entity;

import com.example.domain.JoinRequestState;
import lombok.Data;
import org.hibernate.annotations.Target;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany
    @ElementCollection
    private List<Event> events;
    @ManyToMany
    @Target(Player.class)
    private List<Player> members;
    @OneToMany
//    @ElementCollection
    @JoinColumn(name = "groupId")
    private List<JoinRequest> joinRequests;
    @OneToOne
    private Player admin;

    public boolean containsPlayer(Long playerId) {
        JoinRequest joinRequest = joinRequests.stream()
                .filter(j -> j.getJoinRequestId().getPlayerId().equals(playerId)
                        && j.getJoinRequestState() == JoinRequestState.REQUESTED)
                .findFirst()
                .orElse(null);
        return joinRequest != null;
    }

}