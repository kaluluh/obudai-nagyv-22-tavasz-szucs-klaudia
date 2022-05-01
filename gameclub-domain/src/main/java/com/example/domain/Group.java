package com.example.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Group {

    private Long id;
    private String name;
    private List<Event> events;
    private List<User> members;
    private User admin;

}