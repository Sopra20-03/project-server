package ch.uzh.ifi.seal.soprafs20.exceptions.Game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super("Game with "+message+" doesn't exist.");
    }
}
