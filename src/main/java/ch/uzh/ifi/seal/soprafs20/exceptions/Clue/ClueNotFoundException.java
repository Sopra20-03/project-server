package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ClueNotFoundException extends RuntimeException {
    public ClueNotFoundException(String message) {
        super("Clue with "+message+" doesn't exist.");}
}
