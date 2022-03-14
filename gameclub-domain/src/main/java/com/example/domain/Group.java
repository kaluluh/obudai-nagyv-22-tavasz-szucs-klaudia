package com.example.domain;

import lombok.Data;

import java.util.List;

@Data
public class Group {

    private Long id;
    private String name;
    private List<Event> events;
    private List<User> members;
    private User admin;

}