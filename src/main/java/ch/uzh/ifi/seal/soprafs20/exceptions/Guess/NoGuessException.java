package ch.uzh.ifi.seal.soprafs20.exceptions.Guess;

public class NoGuessException extends RuntimeException {
    public NoGuessException(String message) {
        super(message + " : round has no Guess yet.");
    }
}
