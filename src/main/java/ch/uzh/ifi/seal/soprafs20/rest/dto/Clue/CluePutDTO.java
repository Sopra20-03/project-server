package ch.uzh.ifi.seal.soprafs20.rest.dto.Clue;

import java.util.ArrayList;
import java.util.List;

public class CluePutDTO {
    private String votes;

    public List<Boolean> getVotes() {
        List<Boolean> voteList = new ArrayList<>();
        String[] values = this.votes.split(",");
        for(String value : values) {
            Boolean validated = Boolean.parseBoolean(value);
            voteList.add(validated);
        }
        return voteList;
    }

    public void addVote(Boolean validated) {
        String newVote = "," + validated.toString();
        this.votes = this.votes + newVote;
    }
}
