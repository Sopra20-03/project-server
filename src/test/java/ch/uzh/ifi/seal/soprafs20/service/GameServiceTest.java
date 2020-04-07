package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class GameServiceTest {




    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @BeforeEach
    public void setup(){
        //init testGame

    }
    @Test
    void getGames() {
    }

    @Test
    void getGame() {
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame = gameService.createGame(testGame);
        assertEquals(testGame.getGameId(),gameService.getGame(1L).getGameId());
    }

    @Test
    void createGame() {
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");

        testGame = gameService.createGame(testGame);
        //check if Game Status is set to INITIALIZED when created
        assertEquals(testGame.getGameStatus(),GameStatus.INITIALIZED);
        //check if default GameMode is STANDARD
        assertEquals(testGame.getGameMode(),GameMode.STANDARD);

    }

    @Test
    void addPlayer() {
        //create player to add
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setDateCreated(LocalDate.now());
        testUser.setId(1L);

        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame = gameService.createGame(testGame);

        RealPlayer testPlayer = playerService.createPlayer(testUser, testGame);

        //add player to game
        testGame = gameService.addPlayer(testGame.getGameId(), testPlayer);

        //Assertions
        assertTrue((testGame.getPlayers()).contains(testPlayer));

    }
}