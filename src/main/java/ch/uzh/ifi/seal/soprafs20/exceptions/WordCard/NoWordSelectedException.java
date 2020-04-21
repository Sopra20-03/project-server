package ch.uzh.ifi.seal.soprafs20.exceptions.WordCard;

public class NoWordSelectedException extends RuntimeException {
    public NoWordSelectedException(String message) {super(message + " : has no Word is selected from the WordCard yet."); }
}