package ch.uzh.ifi.seal.soprafs20.rest.dto.Guess;

import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;

import javax.persistence.*;

public class GuessGetDTO {

    private Long guessId;

    private String word;

    private boolean isValid;

    public Long getGuessId() {
        return guessId;
    }

    public void setGuessId(Long guessId) {
        this.guessId = guessId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public boolean getIsValid(){
        return this.isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
