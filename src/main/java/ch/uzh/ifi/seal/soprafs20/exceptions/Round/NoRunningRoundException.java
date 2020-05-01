package ch.uzh.ifi.seal.soprafs20.exceptions.Round;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NoRunningRoundException extends RuntimeException {
    public NoRunningRoundException(String message){super("Game :"+ message+"has no running round");}
}
