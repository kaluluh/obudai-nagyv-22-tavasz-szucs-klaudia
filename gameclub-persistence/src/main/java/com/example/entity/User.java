package com.example.entity;

import com.example.domain.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@MappedSuperclass
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String loginName;
    private String name;
    private String password;
    private String email;
    @ElementCollection
    private List<Role> roles;
}