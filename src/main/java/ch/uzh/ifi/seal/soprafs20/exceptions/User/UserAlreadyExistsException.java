package ch.uzh.ifi.seal.soprafs20.exceptions.User;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(" : User with "+message+" already exists.");
    }
}
