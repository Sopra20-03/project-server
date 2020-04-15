package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuessServiceTest {

    @Autowired
    private GameService gameService;
    @Autowired
    private RoundService roundService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private GuessService guessService;

    public Game testGame;




    @BeforeEach
    void setup(){

    }

    @Test
    void setGuess() {
        //init testGame
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");

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

        Round activeRound = roundService.getRunningRound(testGame);
        guess = guessService.setGuess(activeRound,guess);
        //check if guess is stored in repo and accessible from the game
        assertEquals("testGuess",guessService.getGuess(activeRound).getWord());

    }
}