package com.example;

import com.example.dto.Credentials;
import com.example.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerActionTest extends AbstractTest {

    @Autowired
    private GameClubService gameClubService;

    @Test
    public void shouldLogin() {
        // given
        long expectedId = 1;
        String username = "kovacsp";
        String password = "kp-secret";
        Credentials credentials = new Credentials(username, password);

        // when
        UserDTO user = gameClubService.authenticate(credentials);

        // then
        assertEquals(username, user.getName());
        assertEquals(password, user.getPassword());
        assertEquals(expectedId, user.getId());
    }
}
