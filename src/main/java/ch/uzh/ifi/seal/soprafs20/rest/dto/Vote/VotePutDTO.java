package ch.uzh.ifi.seal.soprafs20.rest.dto.Vote;

public class VotePutDTO {
    private boolean vote;

    public void setVote(boolean vote) {
        this.vote = vote;
    }
    public boolean getVote(){
        return this.vote;
    }
}
