package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
class GameServiceTest {




    @Autowired
    private GameService gameService;

    @BeforeEach
    public void setup(){
        //init testGame

    }
    @Test
    void getGames() {
    }

    @Test
    void getGame() {
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame = gameService.createGame(testGame);
        assertEquals(testGame.getGameId(),gameService.getGame(1L).getGameId());
    }

    @Test
    void createGame() {
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");

        testGame = gameService.createGame(testGame);
        //check if Game Status is set to INITIALIZED when created
        assertEquals(testGame.getGameStatus(),GameStatus.INITIALIZED);
        //check if default GameMode is STANDARD
        assertEquals(testGame.getGameMode(),GameMode.STANDARD);

    }
}