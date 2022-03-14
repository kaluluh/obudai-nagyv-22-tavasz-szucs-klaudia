package com.example.domain;

import lombok.Data;

import java.util.List;

@Data
public class Player extends User{
    private List<Game> games;
}
