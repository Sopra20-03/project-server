package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
class RoundServiceTest {
    @Autowired
    private GameService gameService;
    @Autowired
    private RoundService roundService;
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
        roundService.createRounds(testGame);
        //loads testGame again out of Database
        testGame = gameService.getGame(1L);

        //get rounds
        List<Round> rounds = roundService.getRoundsOfGame(testGame);
        Round round = rounds.get(1);
        //add guess
        round = roundService.setGuess(round.getRoundId(),"testGuess");

        //check if guess is stored in round
        assertEquals("testGuess",round.getGuess());
        //check if guess is stored in repo and accessible from the game
        assertEquals("testGuess",gameService.getGame(1L).getRounds().get(1).getGuess());

    }
}