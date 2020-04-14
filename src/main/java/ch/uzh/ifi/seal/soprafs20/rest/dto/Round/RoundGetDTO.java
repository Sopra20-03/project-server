package ch.uzh.ifi.seal.soprafs20.rest.dto.Round;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;

public class RoundGetDTO {
    private long roundId;
    // private long gameId;
    private int roundNum;
    private Guess guess;

    public long getRoundId() {
        return roundId;
    }

    public void setRoundId(long roundId) {
        this.roundId = roundId;
    }


    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }

    public Guess getGuess() {
        return guess;
    }

    public void setGuess(Guess guess) {
        this.guess = guess;
    }
}
