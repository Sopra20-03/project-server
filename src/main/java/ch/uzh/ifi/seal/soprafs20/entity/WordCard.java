package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@SequenceGenerator(name="wordCardSeq", initialValue=1, allocationSize=100)
public class WordCard implements Serializable {

    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wordCardSeq")
    private long wordCardId;

    @Column
    private String selectedWord;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="wordCardId", nullable = true)
    private Round round;

    @Column(nullable = false)
    private String word1;

    @Column(nullable = false)
    private String word2;

    @Column(nullable = false)
    private String word3;

    @Column(nullable = false)
    private String word4;

    @Column(nullable = false)
    private String word5;

    public WordCard() {
        //empty constructor
    }


    public long getWordCardId() {
        return wordCardId;
    }

    public void setWordCardId(long wordCardId) {
        this.wordCardId = wordCardId;
    }

    public String getSelectedWord() {
        return selectedWord;
    }

    public void setSelectedWord(String selectedWord) {
        this.selectedWord = selectedWord;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public String getWord3() {
        return word3;
    }

    public void setWord3(String word3) {
        this.word3 = word3;
    }

    public String getWord4() {
        return word4;
    }

    public void setWord4(String word4) {
        this.word4 = word4;
    }

    public String getWord5() {
        return word5;
    }

    public void setWord5(String word5) {
        this.word5 = word5;
    }

}
