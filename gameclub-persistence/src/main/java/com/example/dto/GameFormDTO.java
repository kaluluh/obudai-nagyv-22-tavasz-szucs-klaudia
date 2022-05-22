package com.example.dto;

import com.example.domain.Category;
import com.example.entity.Game;
import com.example.entity.Limits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameFormDTO implements TransformableToDomainObject<Game>{
    private String name;
    private String description;
    private String minAge;
    private String noOfPlayersMin;
    private String noOfPlayersMax;
    private String playTimeMin;
    private String playTimeMax;
    private List<String> categories;

    @Override
    public Game toDomainObject() {

       List<Category> categoryList = new ArrayList<>();
       for (String c : categories) {
           Category category = Category.valueOf(c);
           categoryList.add(category);
       }

        return Game.builder()
                .name(name)
                .description(description)
                .minimumAge(Integer.valueOf(minAge))
                .categories(categoryList)
                .playTime(new Limits(Integer.valueOf(playTimeMin),Integer.valueOf(playTimeMax)))
                .numberOfPlayers(new Limits(Integer.valueOf(noOfPlayersMin), Integer.valueOf(noOfPlayersMax)))
                .build();
    }
}
