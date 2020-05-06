package ch.uzh.ifi.seal.soprafs20.rest.dto.User;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;


import javax.persistence.Column;
import java.time.LocalDate;


public class UserGetDTO {

    private Long id;
    private String name;
    private String username;
    private UserStatus status;
    private LocalDate dateCreated;
    private String token;
    private int nrOfPlayedGames;
    private int totalGameScore;
    private int totalIndividualScore;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getTotalIndividualScore() {
        return totalIndividualScore;
    }

    public void setTotalIndividualScore(int totalIndividualScore) {
        this.totalIndividualScore = totalIndividualScore;
    }

    public int getTotalGameScore() {
        return totalGameScore;
    }
    public void setTotalGameScore(int totalGameScore) {
        this.totalGameScore = totalGameScore;
    }

    public int getNrOfPlayedGames() {
        return nrOfPlayedGames;
    }

    public void setNrOfPlayedGames(int nrOfPlayedGames) {
        this.nrOfPlayedGames = nrOfPlayedGames;
    }
}
