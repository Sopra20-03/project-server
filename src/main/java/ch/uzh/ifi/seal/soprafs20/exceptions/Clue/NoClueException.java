package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoClueException extends RuntimeException{
    public NoClueException(String message){super("No Clues in Round with RoundId: "+message);}
}
