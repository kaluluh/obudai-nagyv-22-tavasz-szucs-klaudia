import com.example.GameClubService;
import com.example.domain.Role;
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
        long expectedId = 3;
        String username = "kovacsp";
        String password = "kp-secret";
        Role role = Role.PLAYER;
        Credentials credentials = new Credentials(username, password);

        // when
        UserDTO user = gameClubService.authenticate(credentials);

        // then
        assertEquals(username, user.getName());
        assertEquals(password, user.getPassword());
        assertEquals(expectedId, user.getId());
        assertEquals(role, user.getRoles());
    }

    @Test
    public void shouldJoin() {
        // given

        // when

        // then
    }
}
