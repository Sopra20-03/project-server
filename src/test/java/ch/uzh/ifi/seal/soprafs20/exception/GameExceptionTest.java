package ch.uzh.ifi.seal.soprafs20.exception;

import ch.uzh.ifi.seal.soprafs20.constant.BotMode;
import ch.uzh.ifi.seal.soprafs20.constant.Duration;
import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
public class GameExceptionTest {
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private UserService userService;

    public User testUser;
    public Game testGame;

    @Test
    void GameFullExceptionTest() {
        //try add player when game is already full throws GameFullException
        RealPlayer player1 = new RealPlayer();
        RealPlayer player2 = new RealPlayer();
        RealPlayer player3 = new RealPlayer();
        RealPlayer player4 = new RealPlayer();
        RealPlayer player5 = new RealPlayer();
        List<RealPlayer> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);

        RealPlayer player6 = new RealPlayer();
        User user = new User();
        Game game = new Game();
        game.setPlayers(players);


        Exception exception = assertThrows(GameFullException.class, () -> {
            playerService.addPlayer(game, player6, user);
        });

        String expectedMessage = "Game already has five players.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void GameNotFoundExceptionTest(){
        //if no game exists, GameNotFoundException is thrown
        Exception exception = assertThrows(GameNotFoundException.class, () -> {
            gameService.getGame(1L);
        });

        String expectedMessage = "Game with Id: 1 doesn't exist.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void NotEnoughPlayersExceptionTest(){
        //if not enough players in Game, error is thrown
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame.setDuration(Duration.SHORT);

        testGame = gameService.createGame(testGame);
        Exception exception = assertThrows(NotEnoughPlayersException.class, () -> {
            gameService.startGame(testGame);
        });

        String expectedMessage = "There are only 0 Players in the Game.2 is the minimum Number of Player for a game. Add more Player to the game";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void PlayerAlreadyInGameExceptionTest(){
        //if not enough players in Game, error is thrown
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame.setDuration(Duration.SHORT);

        testGame = gameService.createGame(testGame);
        //create test User
        testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setDateCreated(LocalDate.now());
        testUser.setId(1L);
        testUser = userService.createUser(testUser);


        //create test Player and add to Game
        RealPlayer testPlayer = new RealPlayer();
        testPlayer.setUserId(1L);
        testGame = playerService.addPlayer(testGame, testPlayer, testUser);
        Game testGame2 = new Game();
        //try add player to another game, throws error
        Exception exception = assertThrows(PlayerAlreadyInGameException.class, () -> {
            playerService.addPlayer(testGame2, testPlayer, testUser);
        });

        String expectedMessage = "Player is already in a game,  User with UserId: 1 is in Game with GameId: 1";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void PlayerNotInGameExceptionTest() {
        //create Game
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame.setDuration(Duration.SHORT);
        testGame = gameService.createGame(testGame);
        //create test User
        testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setDateCreated(LocalDate.now());
        testUser.setId(1L);
        testUser = userService.createUser(testUser);
        //remove player throws error because he is not in game

        Exception exception = assertThrows(PlayerNotInGameException.class, () -> {
            playerService.removePlayer(testGame, 1L);
        });

        String expectedMessage = "Player with UserId: 1 is not in the game.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
}