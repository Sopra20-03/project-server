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


    @BeforeEach
    public void setup(){
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");


    }
    @Test
    void createRounds() {
        gameService.createGame(testGame);
        //roundService.createRounds(testGame);
        //check if 2 rounds are created in ROUNDS_T
        assertEquals(2,gameService.getRounds(1L).size());
        assertEquals(2,roundService.getRoundsOfGame(testGame).size());




    }
}