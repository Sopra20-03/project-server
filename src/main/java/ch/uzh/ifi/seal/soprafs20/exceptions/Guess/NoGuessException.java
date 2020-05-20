package ch.uzh.ifi.seal.soprafs20.exceptions.Guess;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NoGuessException extends RuntimeException {
    public NoGuessException(String message) {
        super("Round with " + message + " has no Guess yet.");
    }
}
