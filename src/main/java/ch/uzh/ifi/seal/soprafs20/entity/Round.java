package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_Round")
@SequenceGenerator(name="roundSeq", initialValue=1, allocationSize=100)
public class Round implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roundSeq")
    private Long roundID;

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

}