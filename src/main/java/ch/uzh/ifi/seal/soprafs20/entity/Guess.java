package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;

@Entity
@Table(name = "T_GUESS")
public class Guess {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long guessId;

    @OneToOne(mappedBy = "guess")
    private Round round;

    @Column
    private String word;

    @Column
    private boolean isValid;

    @ManyToOne
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
