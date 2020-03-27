package ch.uzh.ifi.seal.soprafs20.rest.dto.User;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

import java.time.LocalDate;

public class UserPutDTO {
    private String name;
    private String username;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
