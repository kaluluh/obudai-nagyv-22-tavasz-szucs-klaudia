package com.example;

import com.example.entity.Player;
import com.example.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class GameUserDetailsService implements UserDetailsService {

    private PlayerRepository playerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByUserName(username);
        player.getRoles().toString();
        if (player == null) {
            throw new UsernameNotFoundException(username);
        }
        return new GameUserPrincipal(player);
    }
}
