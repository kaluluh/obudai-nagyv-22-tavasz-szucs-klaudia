package com.example;

import com.example.domain.Role;
import com.example.entity.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Transactional
public class GameUserPrincipal implements UserDetails {

    private final Player player;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return player.getRoles();
    }

    @Override
    public String getPassword() {
        return player.getPassword();
    }

    @Override
    public String getUsername() {
        return player.getLoginName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
