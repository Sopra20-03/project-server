package ch.uzh.ifi.seal.soprafs20.exceptions.User;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UsernameTakenException extends RuntimeException {

    public UsernameTakenException(String message) {
        super(message+" : Username already exists.");
    }
}
