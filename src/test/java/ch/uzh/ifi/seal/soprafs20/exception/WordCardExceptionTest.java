package ch.uzh.ifi.seal.soprafs20.exception;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import ch.uzh.ifi.seal.soprafs20.exceptions.WordCard.NoWordSelectedException;
import ch.uzh.ifi.seal.soprafs20.service.GuessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
public class WordCardExceptionTest {

    public Game testGame;
    public WordCard wordCard;
    public Guess guess;
    @Autowired
    private GuessService guessService;

    @Test
    void NoWordSelectedExceptionTest() {
        //correctGuess() throws error when there is no word selected
        wordCard = new WordCard();
        wordCard.setWordCardId(1L);
        guess = new Guess();

        Exception exception = assertThrows(NoWordSelectedException.class, () -> {
            guessService.correctGuess(wordCard, guess);
        });

        String expectedMessage = "WordCard with WordCardId: 1 has no Word selected.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

}