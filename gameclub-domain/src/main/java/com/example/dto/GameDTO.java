package com.example.dto;

import com.example.domain.Category;
import com.example.domain.Limits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private Long id;
    private String name;
    private String description;
    private Integer minimumAge;
    private List<Category> categories;
    private LimitsDTO playTime;
    private Limits numberOfPlayers;

}
