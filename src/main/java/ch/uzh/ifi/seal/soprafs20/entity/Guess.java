package ch.uzh.ifi.seal.soprafs20.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_GUESS")
@SequenceGenerator(name="guessSeq", initialValue=1, allocationSize=100)
public class Guess implements Serializable {

    private static final long serialVersionUID = 5L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guessSeq")
    private Long guessId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="guessId", nullable = true)
    private Round round;

    @Column
    private String word;

    @Column
    private boolean isValid;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playerId")
    private RealPlayer owner;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private long totalTime;

    @Column
    private int score;

    public Long getGuessId() {
        return guessId;
    }

    public void setGuessId(Long guessId) {
        this.guessId = guessId;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
