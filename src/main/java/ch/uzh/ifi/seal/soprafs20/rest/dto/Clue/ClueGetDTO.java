package ch.uzh.ifi.seal.soprafs20.rest.dto.Clue;

public class ClueGetDTO {

    private Long clueId;

    private String word;

    private boolean isValid;

    private int votes;

    private int voteCount;

    private Long ownerId;

    private long totalTime;

    public Long getClueId() {
        return clueId;
    }

    public void setClueId(Long clueId) {
        this.clueId = clueId;
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

    public void setIsValid(boolean valid) {
        isValid = valid;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Long getOwnerId() { return ownerId; }

    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public long getTotalTime() { return totalTime; }

    public void setTotalTime(long totalTime) { this.totalTime = totalTime; }
}
