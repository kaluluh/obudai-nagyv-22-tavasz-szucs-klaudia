package com.example.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue
    private Long Id;
    private String name;
    @OneToMany
    @ElementCollection
    private List<Event> events;
    @OneToMany
    @ElementCollection
    private List<Player> members;
    @ManyToMany
    @ElementCollection
    @JoinColumn(name = "groupId")
    private List<JoinRequest> joinRequests;
    @OneToOne
    private Player admin;

}