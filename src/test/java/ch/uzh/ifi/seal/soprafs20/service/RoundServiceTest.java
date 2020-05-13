package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.constant.BotMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
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
    @MockBean

    private Game testGame;
    private Game testGame2;
    private List<WordCard> cards;
    private Round activeRound;

    @BeforeEach
    public void setup(){
        cards = wordCardService.getWordCards(13);
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame,cards);
    }
/*
    @Test
    void createRounds() {
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        cards = wordCardService.getWordCards(13);
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame,cards);
        //loads testGame again out of Database
        testGame = gameService.getGame(1L);


        //check if 13 rounds are created in ROUNDS_T
        assertEquals(13,roundService.getRoundsOfGame(testGame).size());
        // check if the rounds are stored in GameTable
        assertEquals(13,testGame.getRounds().size());
    }
*/
    @Test
    void startFirstRound(){
        testGame = roundService.startFirstRound(testGame);
        testGame = gameService.getGame(1L);
        //check if first round is running
        assertEquals(RoundStatus.RUNNING, roundService.getRunningRound(testGame).getRoundStatus());
        //test getRunning Round
        assertEquals(1,roundService.getRunningRound(testGame).getRoundNum());
    }

/*
    @Test
    void NoRunningRoundException() {
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        cards = wordCardService.getWordCards(13);
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame,cards);
        //check if exception is thrown when game is not started
        Assertions.assertThatExceptionOfType(NoRunningRoundException.class);
        roundService.getRunningRound(testGame);

    }
*/
}