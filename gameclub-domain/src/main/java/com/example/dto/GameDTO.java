package com.example.dto;

import com.example.domain.Category;
import com.example.domain.Limits;
import lombok.Data;

import java.util.List;

@Data
public class GameDTO {
    private Long id;
    private String name;
    private String description;
    private int minimumAge;
    private List<Category> categories;
    private LimitsDTO playTime;
    private Limits numberOfPlayers;
}
