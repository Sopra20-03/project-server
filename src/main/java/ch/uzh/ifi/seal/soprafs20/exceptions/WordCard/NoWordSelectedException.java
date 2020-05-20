package ch.uzh.ifi.seal.soprafs20.exceptions.WordCard;

public class NoWordSelectedException extends RuntimeException {
    public NoWordSelectedException(String message) {
        super("WordCard with " + message + " has no Word selected.");
    }
}