package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_ROUNDS")
@SequenceGenerator(name="roundSeq", initialValue=1, allocationSize=100)
public class Round implements Serializable {

    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roundSeq")
    private long roundId;

    @Column(nullable = false)
    private RoundStatus roundStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="gameId", nullable = true)
    private Game game;

    @Column(nullable = false)
    private int roundNum;


    @OneToOne(mappedBy = "round", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Guess guess;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "wordCardId")
    private WordCard wordCard;


    /*
    TODO: add these columns

    @Column(nullable = false)
    private RoundStatus status;

    @Column
    private time = startTime;

    @Column
    private time = endTime

    @Column
    private boolean correctGuessed;
     */
    public Round(){}

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }


    public Guess getGuess() {
        return guess;
    }

    public void setGuess(Guess guess) {
        this.guess = guess;
    }

    public RoundStatus getRoundStatus() {
        return roundStatus;
    }

    public void setRoundStatus(RoundStatus roundStatus) {
        this.roundStatus = roundStatus;
    }

    public WordCard getWordCard() { return wordCard; }

    public void setWordCard(WordCard wordCard) { this.wordCard = wordCard; }

}