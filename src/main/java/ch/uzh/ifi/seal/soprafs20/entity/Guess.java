package ch.uzh.ifi.seal.soprafs20.entity;

public class Guess {
    private String word;
    private boolean isValid;
    private RealPlayer owner;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isValid(){
        return this.isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public RealPlayer getOwner() {
        return owner;
    }

    public void setOwner(RealPlayer owner) {
        this.owner = owner;
    }
}
