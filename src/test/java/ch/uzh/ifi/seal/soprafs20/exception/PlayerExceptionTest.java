package ch.uzh.ifi.seal.soprafs20.exception;

import ch.uzh.ifi.seal.soprafs20.exceptions.Player.PlayerNotFoundException;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
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
public class PlayerExceptionTest {
    @Autowired
    private PlayerService playerService;

    @Test
    void PlayerNotFoundException() {
        //getPlayer throws error when there is no player
        Exception exception = assertThrows(PlayerNotFoundException.class, () -> {
            playerService.getPlayerByPlayerId(1L);
        });

        String expectedMessage = "Player with PlayerId: 1 does not exist.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
}