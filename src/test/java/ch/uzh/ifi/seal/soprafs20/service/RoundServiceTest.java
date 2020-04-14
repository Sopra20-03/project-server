package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.NoRunningRoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
class RoundServiceTest {
    @Autowired
    private GameService gameService;
    @Autowired
    private RoundService roundService;
    @Autowired
    private PlayerService playerService;
    @MockBean
    private Game testGame;
    private Game testGame2;


    @BeforeEach
    public void setup(){
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");


    }
    @Test
    void createRounds() {
        testGame = gameService.createGame(testGame);
        roundService.createRounds(testGame);
        //loads testGame again out of Database
        testGame = gameService.getGame(1L);

        //check if 2 rounds are created in ROUNDS_T
        assertEquals(2,roundService.getRoundsOfGame(testGame).size());
        // check if the rounds are stored in GameTable
        assertEquals(2,testGame.getRounds().size());
    }
    @Test
    void addGuess(){
        //create game & rounds
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame);
        testGame = roundService.startFirstRound(testGame);
        //loads testGame again out of Database
        testGame = gameService.getGame(1L);

        //create player
        RealPlayer testPlayer = new RealPlayer();
        testPlayer.setUserId(1L);

        testPlayer = playerService.createPlayer(testPlayer, testGame);


        //add guess
        Guess guess = new Guess();
        guess.setWord("testGuess");
        guess.setOwner(testPlayer);
        Round round = roundService.setGuess(testGame,guess);

        //check if guess is stored in round
        assertEquals("testGuess",round.getGuess().getWord());
        //check if guess is stored in repo and accessible from the game
        assertEquals("testGuess",gameService.getGame(1L).getRounds().get(0).getGuess().getWord());
        //check if guess is accessible in player
        assertEquals("testGuess",playerService.getPlayer(1L).getGuessList().get(0).getWord());

    }

    @Test
    void startFirstRound(){
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame);
        testGame = roundService.startFirstRound(testGame);
        //check if first round is running
        assertEquals(RoundStatus.RUNNING, gameService.getGame(1L).getRounds().get(0).getRoundStatus());
        //check if RoundNum of running Round is 1
        assertEquals(1,roundService.getRunningRound(testGame).getRoundNum());
    }

    @Test
    void NoRunningRoundException() {
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame);
        //check if exception is thrown when game is not started
        roundService.getRunningRound(testGame);
        Assertions.assertThatExceptionOfType(NoRunningRoundException.class);
    }
}