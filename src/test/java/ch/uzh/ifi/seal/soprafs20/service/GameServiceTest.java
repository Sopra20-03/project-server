package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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

        //create Player
        RealPlayer testPlayer = new RealPlayer();
        testPlayer.setUserId(1L);

        //add player to game
        testGame = playerService.addPlayer(testGame,testPlayer, testUser);

        //add game to player
        testPlayer = playerService.createPlayer(testPlayer, testGame);

        //Assertions
        assertTrue((testGame.getPlayers()).contains(testPlayer));
        assertFalse(testGame.getPlayers().isEmpty());
    }

    @Test
    void startGame(){
        //create game
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame = gameService.createGame(testGame);

        //create player to add
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setDateCreated(LocalDate.now());
        testUser.setId(1L);

        //add two players, so the game can be started
        RealPlayer testPlayer1 = new RealPlayer();
        testPlayer1.setUserId(1L);
        testPlayer1 = playerService.createPlayer(testPlayer1, testGame);
        RealPlayer testPlayer2 = new RealPlayer();
        testPlayer2.setUserId(1L);
        testPlayer2= playerService.createPlayer(testPlayer2, testGame);
        playerService.addPlayer(testGame,testPlayer1, testUser);
        playerService.addPlayer(testGame,testPlayer2, testUser);

        // check if game is running after starting it
        assertEquals(1L,testGame.getGameId());
        testGame = gameService.startGame(testGame.getGameId());
        assertEquals(GameStatus.RUNNING, testGame.getGameStatus());
        //check if 2 players are stored in the game
        assertEquals(2,gameService.getGame(testGame.getGameId()).getPlayers().size());
        //check if one player has ROLE.GUESSER
        assertEquals(Role.GUESSER,gameService.getGame(testGame.getGameId()).getPlayers().iterator().next().getRole());

    }

}