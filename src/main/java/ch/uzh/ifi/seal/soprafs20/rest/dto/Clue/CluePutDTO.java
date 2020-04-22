package ch.uzh.ifi.seal.soprafs20.rest.dto.Clue;

import java.util.ArrayList;
import java.util.List;

public class CluePutDTO {
    private boolean votes;

    public void setVotes(boolean votes) {
        this.votes = votes;
    }
    public boolean getVotes(){
        return this.votes;
    }
}
