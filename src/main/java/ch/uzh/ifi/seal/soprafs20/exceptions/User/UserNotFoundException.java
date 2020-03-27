package ch.uzh.ifi.seal.soprafs20.exceptions.User;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(" : User with "+message+" doesn't exist.");
    }
}
