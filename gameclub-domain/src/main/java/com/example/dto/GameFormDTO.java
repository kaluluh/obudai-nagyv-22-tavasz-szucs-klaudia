package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameFormDTO {
    private long id;
    private String name;
    private String description;
    private int minimumAge;
    private String categories;
    private int min;
    private int max;
    private int from;
    private int to;

}
