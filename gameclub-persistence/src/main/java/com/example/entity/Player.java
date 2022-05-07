package com.example.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "players")
public class Player extends User {

    @ManyToMany
    @JoinTable(name = "PLAYERS_GAMES",
            joinColumns = @JoinColumn(name = "PLAYER_ID"),
            inverseJoinColumns = @JoinColumn(name = "GAMES_ID"))
    private List<Game> games;

}
