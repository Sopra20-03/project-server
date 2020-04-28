package ch.uzh.ifi.seal.soprafs20.rest.dto.Player;

import ch.uzh.ifi.seal.soprafs20.constant.Role;

public class PlayerGetDTO {
    private long playerId;
    private long userId;
    private String userName;
    private Role role;
    private int score;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }
}
