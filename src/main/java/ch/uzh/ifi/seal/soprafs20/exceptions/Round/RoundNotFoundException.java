package ch.uzh.ifi.seal.soprafs20.exceptions.Round;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RoundNotFoundException extends RuntimeException {
    public RoundNotFoundException() {
        super(" : Round is not in Game.");
    }
}