package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PlayerIsNotClueWriterException extends RuntimeException {
    public PlayerIsNotClueWriterException(String message) { super(message + " : player does not have the role of Clue Writer."); }
}
