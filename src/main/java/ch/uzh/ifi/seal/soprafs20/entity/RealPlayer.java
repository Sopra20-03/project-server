package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Role;

import javax.persistence.*;
import java.io.Serializable;

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

    @Column
    private Role role;

    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    public Long getPlayerId() { return playerId; }

    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public Game getGame() { return game; }

    public void setGame(Game game) { this.game = game; }
}
