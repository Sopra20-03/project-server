package ch.uzh.ifi.seal.soprafs20.exceptions.Game;

public class GameFullException extends RuntimeException {
    public GameFullException(String message) {
        super(" : Game already has five players.");
    }
}
