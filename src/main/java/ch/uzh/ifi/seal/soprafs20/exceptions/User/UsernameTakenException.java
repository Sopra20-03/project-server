package ch.uzh.ifi.seal.soprafs20.exceptions.User;

public class UsernameTakenException extends RuntimeException {

    public UsernameTakenException(String message) {
        super(message+" : Username already exists.");
    }
}
