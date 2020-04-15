package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.NoRunningRoundException;
import org.assertj.core.api.Assertions;

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
    private List<WordCard> cards;

    @BeforeEach
    public void setup(){



    }
    @Test
    void createRounds() {
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        wordCardService.addAllWordCards();
        cards = wordCardService.getShuffledWordCards();
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame,cards);
        //loads testGame again out of Database
        testGame = gameService.getGame(1L);

        //check if 2 rounds are created in ROUNDS_T
        assertEquals(2,roundService.getRoundsOfGame(testGame).size());
        // check if the rounds are stored in GameTable
        assertEquals(2,testGame.getRounds().size());
    }




    @Test
    void startFirstRound(){
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        wordCardService.addAllWordCards();
        cards = wordCardService.getShuffledWordCards();
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame,cards);
        testGame = roundService.startFirstRound(testGame);
        //check if first round is running
        assertEquals(RoundStatus.RUNNING, gameService.getGame(1L).getRounds().get(0).getRoundStatus());
        //check if RoundNum of running Round is 1
        assertEquals(1,roundService.getRunningRound(testGame).getRoundNum());
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