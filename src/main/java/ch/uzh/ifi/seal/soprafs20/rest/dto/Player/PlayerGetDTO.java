package ch.uzh.ifi.seal.soprafs20.rest.dto.Player;

public class PlayerGetDTO {
    private long playerId;
    private long userId;

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
}
