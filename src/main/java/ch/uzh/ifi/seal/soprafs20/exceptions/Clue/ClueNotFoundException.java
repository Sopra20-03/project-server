package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

public class ClueNotFoundException extends RuntimeException {
    public ClueNotFoundException(String message) {
        super("Clue with "+message+" doesn't exist.");}
}
