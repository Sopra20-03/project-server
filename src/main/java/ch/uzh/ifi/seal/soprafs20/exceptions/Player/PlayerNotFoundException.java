package ch.uzh.ifi.seal.soprafs20.exceptions.Player;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message) {
        super("Player" + message + " does not exist.");
    }
}
