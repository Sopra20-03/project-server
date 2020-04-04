package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * GameControllerTest
 * This is a WebMvcTest which allows to test controller class GameController
 * This allows us to test the REST API calls and validate the request & response
 */
@SpringBootTest
public class GameControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    private Game testGame;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }


    @Test
    public void test() throws Exception {
        System.out.println("Test");
    }

    /*
     * Assertions
     * assertEquals(Expected, Actual)
     */

    //Test case 5 : GET /lobby/games
    /**
     GET /games
     Test: GET /games
     Result: 200 Success & list of games should be given back
     */
    @Test
    public void getGamesSuccess() throws Exception {
        // given
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameStatus(GameStatus.INITIALIZED);

        List<Game> allGames = Collections.singletonList(testGame);

        given(gameService.getGames()).willReturn(allGames);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].gameStatus", is(testGame.getGameStatus().toString())))
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(200, result.getResponse().getStatus());
        //Check Correct HTTP Response Content-Type
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        //Check Correct HTTP Request Data Passing
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getRequest().getContentType());
    }

    //Test case 6 : GET /games/{gameId}
    /**
     GET games/{gameId}
     Test: GET /games/{gameId} with valid game id for which the game exists
     Result: 200 Success with game details
     */
    @Test
    public void getGameSuccess() throws Exception {
        // given
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameStatus(GameStatus.INITIALIZED);

        given(gameService.getGame(Mockito.any())).willReturn(testGame);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/1");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameStatus", is(testGame.getGameStatus().toString())))
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(200, result.getResponse().getStatus());
        //Check Correct HTTP Response Content-Type (Data Format)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        //Check Correct HTTP Response Data
        assertEquals("{\"gameId\":1,\"gameStatus\":\"INITIALIZED\"}", result.getResponse().getContentAsString());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());

    }

    /**
     GET /games/{id}
     Test: GET /games/{id} with invalid game id for which the game doesn't exists
     Result: 404 Not Found Error
     */
    @Test
    public void getGameError() throws Exception {
        // given
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameStatus(GameStatus.INITIALIZED);

        given(gameService.getGame(Mockito.any())).willThrow(GameNotFoundException.class);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/2");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(404, result.getResponse().getStatus());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());

    }

}
