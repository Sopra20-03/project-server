package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
    @Autowired
    private WordCardService wordCardService;

    public Game testGame;
    public List<WordCard> cards;




    @BeforeEach
    void setup(){
        wordCardService.addAllWordCards();
        cards = wordCardService.getShuffledWordCards();
    }

    @Test
    void setGuess() {
        //init testGame
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");

        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame, cards);
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
    @Test
    void validateGuess(){
        Guess guess = new Guess();
        guess.setWord("testWORD");

        WordCard wordCard = new WordCard();
        wordCard.setSelectedWord("testWord");
        assertTrue(guessService.correctGuess(wordCard, guess));
    }
}