package ch.uzh.ifi.seal.soprafs20.rest.dto.Game;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;

public class GamePostDTO {
    private String gameName;
    private String creatorUsername;
    private GameMode gameMode;

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
}
