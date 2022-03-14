package com.example.dto;

import com.example.domain.Role;
import lombok.Data;

import java.util.List;

@Data
public class PlayerDTO {
    private Long id;
    private String loginName;
    private String name;
    private String password;
    private String email;
    private List<Role> roles;
    private List<Long> games;

}
