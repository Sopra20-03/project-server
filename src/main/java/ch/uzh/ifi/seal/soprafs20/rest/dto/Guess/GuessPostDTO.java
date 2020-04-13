package ch.uzh.ifi.seal.soprafs20.rest.dto.Guess;

public class GuessPostDTO {
    private Long userId;
    private String word;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
