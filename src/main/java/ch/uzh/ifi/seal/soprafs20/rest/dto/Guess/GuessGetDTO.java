package ch.uzh.ifi.seal.soprafs20.rest.dto.Guess;

import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;

import javax.persistence.*;

public class GuessGetDTO {

    //private Long guessId;

    private String word;

    //private boolean isValid;


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}
