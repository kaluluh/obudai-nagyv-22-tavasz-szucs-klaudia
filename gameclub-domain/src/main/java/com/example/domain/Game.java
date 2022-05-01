package com.example.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Game {

    private Long id;
    private String name;
    private String description;
    private int minimumAge;
    private List<Category> categories;
    private Limits playTime;
    private Limits numberOfPlayers;
}
