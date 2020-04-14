package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
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
    @Autowired
    private GameService gameService;


    @Test
    void createPlayer() {
        //create test User
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setDateCreated(LocalDate.now());
        testUser.setId(1L);
        //create test Game
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame = gameService.createGame(testGame);
        //create test Player
        RealPlayer testPlayer = new RealPlayer();
        testPlayer.setUserId(1L);



        testPlayer = playerService.createPlayer(testPlayer, testGame);

        assertEquals(testPlayer.getUserId(), testUser.getId());
        assertEquals(testPlayer.getGame(), testGame);
    }
}
