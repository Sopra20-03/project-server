package ch.uzh.ifi.seal.soprafs20.entity;


public class Synonym {
    private String word;
    private int score;

    public Synonym() {
        super();
    }
    public Synonym(String word, int score){
        this.word = word;
        this.score = score;
}

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
