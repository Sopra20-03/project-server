package ch.uzh.ifi.seal.soprafs20.exception;

import ch.uzh.ifi.seal.soprafs20.constant.BotMode;
import ch.uzh.ifi.seal.soprafs20.constant.Duration;
import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.exceptions.Round.NoRunningRoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Round.RoundNotFoundException;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.RoundService;
import org.junit.jupiter.api.BeforeEach;
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
public class RoundExceptionTest {
    Game testGame;
    @Autowired
    private GameService gameService;
    @Autowired
    private RoundService roundService;

    @BeforeEach
    void setup() {
        //create game
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame.setDuration(Duration.SHORT);

        testGame = gameService.createGame(testGame);
    }

    @Test
    void RoundNotFoundException() {
        //getRound with a wrong Round Number throws error


        Exception exception = assertThrows(RoundNotFoundException.class, () -> {
            roundService.getRoundByRoundNum(testGame, 19);
        });

        String expectedMessage = "Round with RoundNum: 19 is not in Game.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void NoRunningRoundExceptionTest() {
        //getRunningRound throws error when there is no running round


        Exception exception = assertThrows(NoRunningRoundException.class, () -> {
            roundService.getRunningRound(testGame);
        });

        String expectedMessage = "Game with GameId: 1 has no running round.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
}