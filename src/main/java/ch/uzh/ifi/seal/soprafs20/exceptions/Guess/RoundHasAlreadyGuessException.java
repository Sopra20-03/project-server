package ch.uzh.ifi.seal.soprafs20.exceptions.Guess;

public class RoundHasAlreadyGuessException extends RuntimeException {
    public RoundHasAlreadyGuessException(String message) {
        super(message + " : round has already Guess.");
    }
}
