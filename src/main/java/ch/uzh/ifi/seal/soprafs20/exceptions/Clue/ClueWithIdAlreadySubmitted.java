package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClueWithIdAlreadySubmitted extends RuntimeException {
    public ClueWithIdAlreadySubmitted(String message) { super("Clue with Id: "+message + " was already submitted."); }
}
