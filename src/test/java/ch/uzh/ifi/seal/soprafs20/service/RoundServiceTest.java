package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


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
    private Game testGame3;
    private List<WordCard> cards;

    @BeforeEach
    public void setup(){



    }

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

    @Test
    void startFirstRound(){
        testGame2 = new Game();
        testGame2.setGameId(2L);
        testGame2.setGameName("testGame");
        cards = wordCardService.getWordCards(13);
        testGame2 = gameService.createGame(testGame2);
        testGame2 = roundService.createRounds(testGame2,cards);

        testGame2 = roundService.startFirstRound(testGame2);
        testGame2 = gameService.getGame(2L);

        //check if first round is running
        assertEquals(RoundStatus.RUNNING, testGame2.getRounds().get(0).getRoundStatus());
        //test getRunning Round
        assertEquals(1,roundService.getRunningRound(testGame2).getRoundNum());
    }


/**
    @Test
    void NoRunningRoundException() {
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        wordCardService.addAllWordCards();
        cards = wordCardService.getShuffledWordCards();
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame,cards);
        //check if exception is thrown when game is not started
        Assertions.assertThatExceptionOfType(NoRunningRoundException.class);
        roundService.getRunningRound(testGame);

    }
    */
}