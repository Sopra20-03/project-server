package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.List;

public class Synonym {
    private String word;
    private List<String> synonyms;

    public Synonym(String word, List<String> synonyms){
        this.word = word;
        this.synonyms = synonyms;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }
}
