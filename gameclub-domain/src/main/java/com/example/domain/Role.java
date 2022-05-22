package com.example.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    PLAYER, GROUP_ADMIN, SUPERUSER;

    @Override
    public String getAuthority() {
        return name();
    }
}
