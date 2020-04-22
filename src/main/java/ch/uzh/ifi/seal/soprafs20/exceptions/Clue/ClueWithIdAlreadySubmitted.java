package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

public class ClueWithIdAlreadySubmitted extends RuntimeException {
    public ClueWithIdAlreadySubmitted(String message) { super(message + " : clue was already submitted."); }
}
