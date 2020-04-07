package ch.uzh.ifi.seal.soprafs20.rest.dto.Round;

import ch.uzh.ifi.seal.soprafs20.entity.Game;

public class RoundGetDTO {
    private long roundId;
    private long gameId;
    private int roundNum;

    public long getRoundId() {
        return roundId;
    }

    public void setRoundId(long roundId) {
        this.roundId = roundId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }
}
