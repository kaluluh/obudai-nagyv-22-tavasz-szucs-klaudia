package com.example.domain;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private Long id;
    private String loginName;
    private String name;
    private String password;
    private String email;
    private List<Role> roles;
}