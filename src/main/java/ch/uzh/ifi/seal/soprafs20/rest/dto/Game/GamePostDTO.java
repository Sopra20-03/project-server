package ch.uzh.ifi.seal.soprafs20.rest.dto.Game;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;

public class GamePostDTO {
    private String gameName;
    private GameMode gameMode;

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
