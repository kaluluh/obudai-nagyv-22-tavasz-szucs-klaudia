package com.example.dto;

import com.example.domain.Role;
import com.example.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO implements TransformableToDomainObject<User> {
    private Long id;
    private String loginName;
    private String name;
    private String password;
    private String email;
    private List<Role> roles;
    private List<Long> games;

    @Override
    public User toDomainObject() {
        return User.builder()
                .id(id)
                .loginName(loginName)
                .name(name)
                .password(password)
                .email(email)
                .roles(roles)
                .build();
    }
}
