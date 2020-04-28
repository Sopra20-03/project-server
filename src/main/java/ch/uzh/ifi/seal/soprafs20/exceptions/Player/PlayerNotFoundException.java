package ch.uzh.ifi.seal.soprafs20.exceptions.Player;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message) {super("Player with PlayerId:"+message +"does not exist.");
    }
}
