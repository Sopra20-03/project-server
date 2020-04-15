package ch.uzh.ifi.seal.soprafs20.rest.dto.Clue;

public class ClueGetDTO {

    private Long clueId;

    private String word;

    private boolean isValid;

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
}
