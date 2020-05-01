package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClueNotInRoundException extends RuntimeException{
    public ClueNotInRoundException(String clueId, String roundId) { super("clue: " + clueId + " not in round: " + roundId); }
}
