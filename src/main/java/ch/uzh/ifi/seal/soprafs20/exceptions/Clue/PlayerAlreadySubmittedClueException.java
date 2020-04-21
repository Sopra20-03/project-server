package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

public class PlayerAlreadySubmittedClueException extends RuntimeException {
    public PlayerAlreadySubmittedClueException(String message) { super(message + " : player already submitted a guess."); }
}
