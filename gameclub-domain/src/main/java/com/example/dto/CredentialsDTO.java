package com.example.dto;

import com.example.domain.Role;
import lombok.Data;

import java.util.List;

@Data
public class CredentialsDTO {

    public String userName;
    public String password;
    private List<Role> roles;

    public CredentialsDTO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
