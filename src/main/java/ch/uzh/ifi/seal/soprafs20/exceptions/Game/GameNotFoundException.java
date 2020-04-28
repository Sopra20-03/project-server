package ch.uzh.ifi.seal.soprafs20.exceptions.Game;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super("Game with "+message+" doesn't exist.");
    }
}
