package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.BotMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameService gameService;
    @Autowired
    private UserService userService;

    public Game testGame;
    public User testUser;
    public RealPlayer testPlayer;


    @BeforeEach
    void setup() {
        //create test Game
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
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
        testPlayer = new RealPlayer();
        testPlayer.setUserId(1L);
        testGame = playerService.addPlayer(testGame, testPlayer, testUser);
    }

    @Test
    void addPlayerTest() {
        assertEquals(testPlayer.getUserId(), testUser.getId());
        assertEquals(testPlayer.getGame(), testGame);
    }

    @Test
    void removePlayer(){



        //check number of players in game before and after removing
        assertEquals(1,playerService.getPlayersByGame(testGame).size());
        playerService.removePlayer(testGame,1L);
        assertEquals(0,playerService.getPlayersByGame(testGame).size());
    }

    @Test
    void getTotalIndividualScore(){

        assertEquals(0, playerService.getTotalIndividualScore(testUser.getId()));
    }

    @Test
    void getTotalNumberOfGames() {

        assertEquals(1, playerService.getNumberOfPlayedGames(testUser.getId()));
    }

    @Test
    void getTotalGameScore() {
        //check gamescore
        assertEquals(0, playerService.getTotalGameScore(testUser.getId()));
    }

    @Test
    void setScoreTest() {
        //check if score is set
        playerService.setScore(1L, 1000);
        assertEquals(1000, playerService.getPlayerByPlayerId(1L).getScore());
    }

    @Test
    void removeAllPlayerTest() {

        assertEquals(1, playerService.getPlayersByGame(testGame).size());
        //check if all players are removed
        playerService.removeAllPlayer(testGame);
        assertEquals(0, playerService.getPlayersByGame(testGame).size());
    }

    @Test
    void removeScoreTest() {
        playerService.setScore(1L, 1000);
        assertEquals(1000, playerService.getPlayerByPlayerId(1L).getScore());
        playerService.removeScore(testPlayer, 10);
        assertEquals(990, testPlayer.getScore());
    }

}
