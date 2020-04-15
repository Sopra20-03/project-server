package ch.uzh.ifi.seal.soprafs20.rest.dto.WordCard;

public class WordCardPutDTO {
    private String selectedWord;

    public void setSelectedWord(String selectedWord) {
        this.selectedWord = selectedWord;
    }

    public String getSelectedWord() { return selectedWord; }
}