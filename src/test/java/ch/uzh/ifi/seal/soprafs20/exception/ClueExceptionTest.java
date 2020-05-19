package ch.uzh.ifi.seal.soprafs20.exception;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.ClueNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.ClueNotInRoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.ClueWithIdAlreadySubmitted;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.NoClueException;
import ch.uzh.ifi.seal.soprafs20.service.ClueService;
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
public class ClueExceptionTest {
    @Autowired
    private ClueService clueService;
    @Test
    void ClueNotFoundExceptionTest(){
        //clue doesn't exist, so ClueNotFoundException is thrown
        Exception exception = assertThrows(ClueNotFoundException.class, () -> {
            clueService.getClueById(1L);
        });

        String expectedMessage = "Clue with Clue Id:1 doesn't exist.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void ClueNotInRoundExceptionTest(){
        //clue is not added to round, so ClueNotInRoundException is thrown
        Round round = new Round();
        round.setRoundId(1L);
        Clue clue = new Clue();
        clue.setClueId(1L);
        Exception exception = assertThrows(ClueNotInRoundException.class, () -> {
            clueService.calculateIndividualScore(round, clue);
        });

        String expectedMessage = "clue: 1 not in round: 1";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void ClueWithIdAlreadySubmittedExceptionTest(){
        Clue clue = new Clue();
        clue.setClueId(1L);
        clue.setWord("testClue");
        RealPlayer owner = new RealPlayer();
        owner.setRole(Role.CLUE_WRITER);
        String word = "testWord";
        //if clue has already a word set, ClueWithIdAlreadySubmitted error is thrown
        Exception exception = assertThrows(ClueWithIdAlreadySubmitted.class, () -> {
            clueService.submitClue(clue,owner,word);
        });

        String expectedMessage = "Clue with Id: 1 was already submitted.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void NoClueExceptionTest(){
        //get round has no clues, so throws NoClueException
        Round round = new Round();
        round.setRoundId(1L);

        Exception exception = assertThrows(NoClueException.class, () -> {
            clueService.getClues(round);
        });

        String expectedMessage = "No Clues in Round with RoundId: 1";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
