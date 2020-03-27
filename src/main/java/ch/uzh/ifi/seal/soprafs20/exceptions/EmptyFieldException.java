package ch.uzh.ifi.seal.soprafs20.exceptions;

public class EmptyFieldException extends RuntimeException {
    public EmptyFieldException(String message) {
        super(message+" field should not be empty.");
    }
}
