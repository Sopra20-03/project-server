package ch.uzh.ifi.seal.soprafs20.exceptions.Game;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotEnoughPlayersException extends RuntimeException {
    public NotEnoughPlayersException(String message) {super(message + " is the minimum Number of Player for a game. Add more Player to the game"); }
}

