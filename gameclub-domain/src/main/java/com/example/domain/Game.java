package com.example.domain;

import lombok.Data;

import java.util.List;

@Data
public class Game {

    private Long id;
    private String name;
    private String description;
    private int minimumAge;
    private List<Category> categories;
    private Limits playTime;
    private Limits numberOfPlayers;
}
