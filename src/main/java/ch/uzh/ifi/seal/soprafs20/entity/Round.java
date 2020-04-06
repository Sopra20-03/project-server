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
    private long Id;

    @ManyToOne
    @JoinColumn(name="gameId")
    private Game game;

    @Column(nullable = false)
    private int roundNum;

    /*
    TODO: add these columns

    @Column(nullable = false)
    private WordCard card;

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
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
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
}