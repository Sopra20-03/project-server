package ch.uzh.ifi.seal.soprafs20.exceptions.Game;

public class PlayerAlreadyInGameException extends RuntimeException {
    public PlayerAlreadyInGameException(String message) { super(" : Player with"+message+"is already in the game"); }
}
