package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private Date dateNow;
    private SimpleDateFormat ft;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setGameMode(GameMode.RIVAL);
        testGame.setGameStatus(GameStatus.INITIALIZED);
        dateNow = new Date();
        testGame.setTimeCreated(dateNow);
        //init Time format
        ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss");
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
                .andExpect(jsonPath("$[0].gameName", is(testGame.getGameName())))
                .andExpect(jsonPath("$[0].gameStatus", is(testGame.getGameStatus().toString())))
                .andExpect(jsonPath("$[0].gameMode", is(testGame.getGameMode().toString())))
                .andExpect(jsonPath("$[0].timeCreated", is(ft.format(testGame.getTimeCreated()))))
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
        given(gameService.getGame(Mockito.any())).willReturn(testGame);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/1");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameName", is(testGame.getGameName())))
                .andExpect(jsonPath("$.gameStatus", is(testGame.getGameStatus().toString())))
                .andExpect(jsonPath("$.gameMode", is(testGame.getGameMode().toString())))
                .andExpect(jsonPath("$.timeCreated", is(ft.format(testGame.getTimeCreated()))))
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(200, result.getResponse().getStatus());
        //Check Correct HTTP Response Content-Type (Data Format)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        //Check Correct HTTP Response Data
        assertEquals("{\"gameId\":1,\"gameName\":\"testGame\",\"gameStatus\":\"INITIALIZED\",\"gameMode\":\"RIVAL\",\"timeCreated\":\""+ ft.format(dateNow)+"\"}", result.getResponse().getContentAsString());
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

    /**
     POST /games
     Test: POST /games with valid data
     Result: 201 Created and Successfully added a game
     */
    @Test
    public void createGameSuccess() throws Exception {

        //Game from PostDTO
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setGameMode(GameMode.RIVAL);


        given(gameService.createGame(Mockito.any())).willReturn(testGame);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO))
                .characterEncoding("utf-8");

        // then
        MvcResult result = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameName", is(testGame.getGameName())))
                .andExpect(jsonPath("$.gameStatus", is(testGame.getGameStatus().toString())))
                .andExpect(jsonPath("$.gameMode", is(testGame.getGameMode().toString())))
                .andExpect(jsonPath("$.timeCreated", is(ft.format(testGame.getTimeCreated()))))
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(201, result.getResponse().getStatus());
        //Check Correct HTTP Response Content-Type (Data Format)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        //Check Correct HTTP Response Data
        assertEquals("{\"gameId\":1,\"gameName\":\"testGame\",\"gameStatus\":\"INITIALIZED\",\"gameMode\":\"RIVAL\",\"timeCreated\":\""+ ft.format(dateNow)+"\"}", result.getResponse().getContentAsString());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.POST.name(), result.getRequest().getMethod());
        //Check Correct HTTP Request Data Passing
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getRequest().getContentType());
    }


    /**
     * Helper Method to convert gamePostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"gameMode": "RIVAL"}
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }




}

