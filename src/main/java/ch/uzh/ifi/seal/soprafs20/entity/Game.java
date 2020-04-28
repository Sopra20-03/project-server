package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_GAMES")
@SequenceGenerator(name="gameSeq", initialValue=1, allocationSize=100)
public class Game implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gameSeq")
    private long gameId;

    @Column(nullable = false)
    private String gameName;

    @Column
    private String creatorUsername;

    @Column(nullable = false)
    private LocalDate dateCreated;

    @Column
    private int playerCount;

    @Column(nullable = false)
    private GameStatus gameStatus;

    @Column(nullable = false)
    private GameMode gameMode;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "game", cascade = CascadeType.MERGE)
    private List<Round> rounds = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "game", cascade = CascadeType.MERGE)
    private List<RealPlayer> players = new ArrayList<>();

    @Column(nullable = false)
    private int score;

    public Game() {
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public List<RealPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<RealPlayer> players) {
        this.players = players;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

}