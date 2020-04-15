package ch.uzh.ifi.seal.soprafs20.rest.dto.Round;


import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;


public class RoundGetDTO {
    private long roundId;
    private int roundNum;
    private RoundStatus roundStatus;
    private WordCard wordCard;

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


    public RoundStatus getRoundStatus() {
        return roundStatus;
    }

    public void setRoundStatus(RoundStatus roundStatus) {
        this.roundStatus = roundStatus;
    }

    public WordCard getWordCard() { return wordCard; }

    public void setWordCard(WordCard wordCard) { this.wordCard = wordCard; }

}
