package com.example.dto;

import com.example.domain.Category;
import com.example.domain.Game;
import com.example.domain.Limits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO implements TransformableToDomainObject<Game>  {
    private Long id;
    private String name;
    private String description;
    private Integer minimumAge;
    private List<Category> categories;
    private LimitsDTO playTime;
    private Limits numberOfPlayers;

    @Override
    public Game toDomainObject() {
        return Game.builder()
                .id(id)
                .name(name)
                .description(description)
                .minimumAge(minimumAge)
                .categories(categories)
                .playTime(playTime.toDomainObject())
                .numberOfPlayers(numberOfPlayers)
                .build();
    }
}
