package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
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

    @AfterEach
    public void tearDown() {

    }

    @Test
    void getGame() {
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame.setDuration(Duration.SHORT);
        testGame = gameService.createGame(testGame);
        assertEquals(testGame.getGameId(),gameService.getGame(1L).getGameId());
    }

    @Test
    void createGame() {
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame.setDuration(Duration.SHORT);

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
        testUser.setId(5L);

        //create game
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame = gameService.createGame(testGame);

        //create Player
        RealPlayer testPlayer = new RealPlayer();
        testPlayer.setUserId(5L);

        //add player to game
        testGame = playerService.addPlayer(testGame,testPlayer, testUser);

        //add game to player
        testPlayer = playerService.createPlayer(testPlayer, testGame);

        //Assertions
        assertTrue((testGame.getPlayers()).contains(testPlayer));
        assertFalse(testGame.getPlayers().isEmpty());
    }

    @Test
    void getGamesTest() {
        //create game1
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setCreatorUsername("testUser");
        testGame.setBotMode(BotMode.FRIENDLY);
        gameService.createGame(testGame);
        //create game2
        Game testGame2 = new Game();
        testGame2.setGameId(2L);
        testGame2.setCreatorUsername("testUser");
        testGame2.setBotMode(BotMode.FRIENDLY);
        gameService.createGame(testGame2);
        //check if getGames() return both created games
        assertEquals(2, gameService.getGames().size());
    }

    @Test
    void removeGameTest() {
        //create game
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setCreatorUsername("testUser");
        testGame.setBotMode(BotMode.FRIENDLY);
        gameService.createGame(testGame);
        //remove game
        gameService.removeGame(testGame);
        assertEquals(0, gameService.getGames().size());
    }

    @Test
    void increaseScoreTest() {
        //create game
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setCreatorUsername("testUser");
        testGame.setBotMode(BotMode.FRIENDLY);
        gameService.createGame(testGame);
        assertEquals(0, testGame.getScore());
        //increase GameScore
        gameService.increaseScore(testGame);
        assertEquals(1, testGame.getScore());
    }
}