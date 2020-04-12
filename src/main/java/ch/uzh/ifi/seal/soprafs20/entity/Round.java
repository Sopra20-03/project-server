package ch.uzh.ifi.seal.soprafs20.entity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="gameId", nullable = true)
    private Game game;

    @Column(nullable = false)
    private int roundNum;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "wordCardId")
    @Column
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

    public WordCard getWordCard() { return wordCard; }

    public void setWordCard(WordCard wordCard) { this.wordCard = wordCard; }
}