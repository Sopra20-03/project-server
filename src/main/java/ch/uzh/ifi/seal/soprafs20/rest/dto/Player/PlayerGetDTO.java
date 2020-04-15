package ch.uzh.ifi.seal.soprafs20.rest.dto.Player;

import ch.uzh.ifi.seal.soprafs20.constant.Role;

public class PlayerGetDTO {
    private long playerId;
    private long userId;
    private Role role;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
