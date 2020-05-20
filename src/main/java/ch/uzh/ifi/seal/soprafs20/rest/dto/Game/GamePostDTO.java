package ch.uzh.ifi.seal.soprafs20.rest.dto.Game;

import ch.uzh.ifi.seal.soprafs20.constant.BotMode;
import ch.uzh.ifi.seal.soprafs20.constant.Duration;
import ch.uzh.ifi.seal.soprafs20.constant.GameMode;

public class GamePostDTO {
    private String gameName;
    private String creatorUsername;
    private GameMode gameMode;
    private BotMode botMode;
    private Duration duration;

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setBotMode(BotMode botMode) { this.botMode = botMode; }

    public BotMode getBotMode() { return botMode; }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }
}
