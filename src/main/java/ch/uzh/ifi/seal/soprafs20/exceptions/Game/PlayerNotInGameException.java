package ch.uzh.ifi.seal.soprafs20.exceptions.Game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Player not in Game")
public class PlayerNotInGameException extends RuntimeException {
    public PlayerNotInGameException(String message) {
        super("Player with " + message + " is not in the game.");
    }
}
