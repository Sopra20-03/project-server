package ch.uzh.ifi.seal.soprafs20.exceptions.Game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Player already in Game")
public class PlayerAlreadyInGameException extends RuntimeException {

    public PlayerAlreadyInGameException(String message) { super(" Player is already in a game, "+message+""); }

}
