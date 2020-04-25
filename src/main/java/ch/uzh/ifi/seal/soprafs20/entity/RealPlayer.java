package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_Players")
@SequenceGenerator(name="playerSeq", initialValue=1, allocationSize=100)
public class RealPlayer implements Serializable {

    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playerSeq")
    private Long playerId;

    @Column (nullable = false)
    private Long userId;

    @Column (nullable = false)
    private String userName;

    @Column
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameId")
    private Game game;

    @Column
    private int score;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Guess> guessList = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.MERGE)
    private List<Clue> clues = new ArrayList<>();

    public Long getPlayerId() { return playerId; }

    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public Game getGame() { return game; }

    public void setGame(Game game) { this.game = game; }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    public List<Guess> getGuessList() {
        return guessList;
    }

    public void addGuess(Guess guess){
        this.guessList.add(guess);
    }

    public List<Clue> getClues() { return clues; }

    public void addClue(Clue clue) { this.clues.add(clue); }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
