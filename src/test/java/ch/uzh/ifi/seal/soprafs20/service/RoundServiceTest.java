package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}