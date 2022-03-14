package com.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupDTO {
    private Long id;
    private String name;
    private Long admin;
    private List<Long> members;

}
