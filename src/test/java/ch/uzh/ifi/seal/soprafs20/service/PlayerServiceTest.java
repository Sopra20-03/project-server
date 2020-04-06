package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    @Test
    void createPlayer() {
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setId(1L);
        testUser.setDateCreated(LocalDate.now());

        RealPlayer testPlayer = new RealPlayer();
        testPlayer.setRole(Role.CLUE_WRITER);
        testPlayer.setPlayerId(1L);
        testPlayer.setUser(testUser);


        testPlayer = playerService.createPlayer(testUser);

        assertEquals(testPlayer.getUser(), testUser);
        assertEquals(testPlayer.getRole(), Role.CLUE_WRITER);
        assertEquals(testPlayer.getPlayerId(), 1L);
    }
}
