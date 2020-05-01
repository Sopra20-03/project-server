package ch.uzh.ifi.seal.soprafs20.exceptions.Guess;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RoundHasAlreadyGuessException extends RuntimeException {
    public RoundHasAlreadyGuessException(String message) {
        super(message + " has already Guess.");
    }
}
