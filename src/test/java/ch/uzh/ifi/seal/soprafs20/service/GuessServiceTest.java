package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.BotMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
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
    public Round activeRound;
    public RealPlayer testPlayer1;
    public RealPlayer testPlayer2;
    public Guess guess1;
    public Guess guess2;

    @BeforeEach
    void setup(){
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
        List<Round> rounds = roundService.getRoundsOfGame(testGame);
        activeRound = rounds.get(0);
        activeRound.setRoundStatus(RoundStatus.RUNNING);
    }

    @Test
    void setGuess() {
        //create player
        testPlayer1 = new RealPlayer();
        testPlayer1.setUserName("testUser1");
        testPlayer1.setUserId(1L);
        testPlayer1.setRole(Role.GUESSER);
        testPlayer1 = playerService.createPlayer(testPlayer1, testGame);
        //select word from wordcard
        wordCardService.selectWord(activeRound, "testWord");
        //create guess
        guess1 = new Guess();
        guess1.setWord("testGuess1");
        guess1.setOwner(testPlayer1);
        guess1 = guessService.setGuess(activeRound, guess1);
        //check if guess is stored in repo and accessible from the game
        assertEquals("testGuess1",guessService.getGuess(activeRound).getWord());
    }

    @Test
    void setCorrectGuess() {
        //create player
        testPlayer2 = new RealPlayer();
        testPlayer2.setUserName("testUser2");
        testPlayer2.setUserId(2L);
        testPlayer2.setRole(Role.GUESSER);
        testPlayer2 = playerService.createPlayer(testPlayer2, testGame);
        //select word from wordcard
        wordCardService.selectWord(activeRound, "testWord");
        //create guess
        guess2 = new Guess();
        guess2.setWord("testWord");
        guess2.setOwner(testPlayer2);
        guess2 = guessService.setGuess(activeRound, guess2);
        //check if guess is stored in repo and accessible from the game
        assertTrue(guess2.getIsValid());
    }

    @Test
    void setIncorrectGuess() {
        //create player
        testPlayer2 = new RealPlayer();
        testPlayer2.setUserName("testUser2");
        testPlayer2.setUserId(2L);
        testPlayer2.setRole(Role.GUESSER);
        testPlayer2 = playerService.createPlayer(testPlayer2, testGame);
        //select word from wordcard
        wordCardService.selectWord(activeRound, "testWord");
        //create guess
        guess2 = new Guess();
        guess2.setWord("NotTestWord");
        guess2.setOwner(testPlayer2);
        guess2 = guessService.setGuess(activeRound, guess2);
        //check if guess is stored in repo and accessible from the game
        assertFalse(guess2.getIsValid());
    }

    /*
    @Test
    void validateGuess() {
        Guess guess = new Guess();
        guess.setWord("testWORD");

        WordCard wordCard = new WordCard();
        wordCard.setSelectedWord("testWord");
        assertTrue(guessService.correctGuess(wordCard, guess));
    }
     */
}