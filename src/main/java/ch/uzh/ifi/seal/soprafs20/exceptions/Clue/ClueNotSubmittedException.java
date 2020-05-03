package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ClueNotSubmittedException extends RuntimeException {
    public ClueNotSubmittedException(String message) {
        super("Clue with id: " +(message) + "hasn't been submitted yet");
    }
}
