package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotEnoughCluesException extends RuntimeException{
    public NotEnoughCluesException(String message){super(message);}
}
