package com.example.dto;

import com.example.domain.Category;
import com.example.entity.Game;
import com.example.entity.Limits;
import com.example.validation.TimeRangeConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TimeRangeConstraint(message = "*Maximum must be greater than minimum")
public class GameFormDTO implements TransformableToDomainObject<Game>{
    @NotEmpty(message = "*Name must be filled")
    private String name;
    @NotEmpty(message = "*Description must be filled")
    private String description;
    @Min(value = 3,message = "*Minimum age must be greater than or equal to three")
    private String minAge;
    @Positive(message = "*Numbers of players minimum must be greater than zero")
    @NotNull(message = "*Value must be filled")
    private Integer noOfPlayersMin;
    @NotNull(message = "*Value must be filled")
    private Integer noOfPlayersMax;
    @Positive(message = "*Playtime minimum must be greater than zero")
    @NotNull(message = "*Value must be filled")
    private Integer playTimeMin;
    @NotNull(message = "*Value must be filled")
    private Integer playTimeMax;
    @NotEmpty(message = "*Field must be filled")
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
                .playTime(new Limits(playTimeMin,playTimeMax))
                .numberOfPlayers(new Limits(noOfPlayersMin, noOfPlayersMax))
                .build();
    }
}
