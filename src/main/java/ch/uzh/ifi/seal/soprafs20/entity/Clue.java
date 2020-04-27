package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_CLUES")
@SequenceGenerator(name="clueSeq", initialValue=1, allocationSize=100)
public class Clue implements Serializable {

    private static final long serialVersionUID = 7L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clueSeq")
    private Long clueId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roundId")
    private Round round;

    @Column
    private String word;

    @Column
    private boolean isValid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playerId")
    private RealPlayer owner;

    @Column
    private Long ownerId;

    @Column
    private int votes;

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

    public RealPlayer getOwner() {
        return owner;
    }

    public void setOwner(RealPlayer owner) {
        this.owner = owner;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

}
