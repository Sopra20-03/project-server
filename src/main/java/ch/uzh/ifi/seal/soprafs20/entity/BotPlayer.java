package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_BotPlayers")
@SequenceGenerator(name="botPlayerSeq", initialValue=1, allocationSize=100)
public class BotPlayer implements Serializable {

    private static final long serialVersionUID = 11L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "botPlayerSeq")
    private Long playerId;


    @Column (nullable = false)
    private String userName;

    @Column
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameId")
    private Game game;


    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "ownerBot", cascade = CascadeType.MERGE)
    private List<Clue> clues = new ArrayList<>();

    public Long getPlayerId() { return playerId; }

    public void setPlayerId(Long playerId) { this.playerId = playerId; }


    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public Game getGame() { return game; }

    public void setGame(Game game) { this.game = game; }


    public List<Clue> getClues() { return clues; }

    public void addClue(Clue clue) { this.clues.add(clue); }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
