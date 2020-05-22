package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.constant.BotMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional

@SpringBootTest
class RoundServiceTest {
    @Autowired
    private GameService gameService;
    @Autowired
    private RoundService roundService;
    @Autowired
    private WordCardService wordCardService;
    @Autowired
    private UserService userService;
    @Autowired
    private PlayerService playerService;
    @MockBean

    private Game testGame;

    private List<WordCard> cards;
    private User testUser1;
    private User testUser2;
    private RealPlayer testPlayer1;
    private RealPlayer testPlayer2;

    @BeforeEach
    public void setup() {
        cards = wordCardService.getWordCards(13);
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame, cards);
        cards = wordCardService.getWordCards(13);
    }

    @Test
    void createRounds() {
        //loads testGame again out of Database
        testGame = gameService.getGame(1L);

        //check if 13 rounds are created in ROUNDS_T
        assertEquals(3,roundService.getRoundsOfGame(testGame).size());

    }

    @Test
    void startFirstRound() {
        testGame = roundService.startFirstRound(testGame);
        testGame = gameService.getGame(1L);
        //check if first round is running
        assertEquals(RoundStatus.RUNNING, roundService.getRunningRound(testGame).getRoundStatus());
        //test getRunning Round
        assertEquals(1, roundService.getRunningRound(testGame).getRoundNum());
    }

    @Test
    void removeRoundsTest() {
        //remove rounds
        testGame = roundService.removeRounds(testGame);
        //check if game is without rounds
        assertEquals(0, roundService.getRoundsOfGame(testGame).size());
    }

    @Test
    void startNextRoundTest(){
        //setup game
        //create test User1 & add to game
        testUser1 = new User();
        testUser1.setName("testName");
        testUser1.setUsername("testUsername");
        testUser1.setPassword("testPassword");
        testUser1.setToken("testToken");
        testUser1.setStatus(UserStatus.OFFLINE);
        testUser1.setDateCreated(LocalDate.now());
        testUser1.setId(1L);
        testUser1 = userService.createUser(testUser1);
        testPlayer1 = new RealPlayer();
        testPlayer1.setUserId(1L);
        testGame = playerService.addPlayer(testGame, testPlayer1, testUser1);
        //create test User2 & add to game
        testUser2 = new User();
        testUser2.setName("testName");
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword");
        testUser2.setToken("testToken");
        testUser2.setStatus(UserStatus.OFFLINE);
        testUser2.setDateCreated(LocalDate.now());
        testUser2.setId(2L);
        testUser2 = userService.createUser(testUser2);
        testPlayer2 = new RealPlayer();
        testPlayer2.setUserId(2L);
        testGame = playerService.addPlayer(testGame, testPlayer2, testUser2);
        //start game
        testGame = gameService.startGame(testGame);
        testGame = roundService.startFirstRound(testGame);

        //check current running Round
        assertEquals(1, roundService.getRunningRound(testGame).getRoundNum());
        testGame = roundService.startNextRound(testGame);
        //check if next round is running now
        assertEquals(2, roundService.getRunningRound(testGame).getRoundNum());

    }


}