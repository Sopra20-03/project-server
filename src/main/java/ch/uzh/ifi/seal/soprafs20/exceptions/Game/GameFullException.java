package ch.uzh.ifi.seal.soprafs20.exceptions.Game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class GameFullException extends RuntimeException {
    public GameFullException(String message) {
        super(" : Game already has five players.");
    }
}
