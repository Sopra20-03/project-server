package ch.uzh.ifi.seal.soprafs20.exception;

import ch.uzh.ifi.seal.soprafs20.constant.BotMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.Guess.NoGuessException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Guess.RoundHasAlreadyGuessException;
import ch.uzh.ifi.seal.soprafs20.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
public class GuessExceptionTest {
    Round activeRound;
    Game testGame;
    List<WordCard> cards;
    Guess guess1;
    Guess guess2;
    RealPlayer testPlayer1;
    @Autowired
    private GuessService guessService;
    @Autowired
    private WordCardService wordCardService;
    @Autowired
    private GameService gameService;
    @Autowired
    private RoundService roundService;
    @Autowired
    private PlayerService playerService;

    @BeforeEach
    void setup() {
        cards = wordCardService.getWordCards(13);
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame, cards);
        List<Round> rounds = roundService.getRoundsOfGame(testGame);
        activeRound = rounds.get(0);
        activeRound.setRoundStatus(RoundStatus.RUNNING);
    }

    @Test
    void NoGuessExceptionTest() {
        //getGuess() of round throws error because no guess is set
        Exception exception = assertThrows(NoGuessException.class, () -> {
            guessService.getGuess(activeRound);
        });

        String expectedMessage = "Round with Round Number: 1 has no Guess yet.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void RoundHasAlreadyGuessExceptionTest() {
        //setGuess() throws error because Round already have Guess

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
        guess2 = new Guess();
        //try add second guess
        Exception exception = assertThrows(RoundHasAlreadyGuessException.class, () -> {
            guessService.setGuess(activeRound, guess2);
        });

        String expectedMessage = "Round with Round Number: 1 has already Guess.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
}