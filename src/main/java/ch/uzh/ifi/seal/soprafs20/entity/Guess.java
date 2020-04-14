package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_GUESS")
@SequenceGenerator(name="guessSeq", initialValue=1, allocationSize=100)
public class Guess implements Serializable {

    private static final long serialVersionUID = 4L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guessSeq")
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
