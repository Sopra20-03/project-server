package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.constant.BotMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UserAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Message.MessagePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.ChatService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
class ChatControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private Message testMessage;
    private LocalDateTime time = LocalDateTime.MAX;
    private Game testGame;


    @MockBean
    private ChatService chatService;
    @MockBean
    private GameService gameService;


    @BeforeEach
    public void setup(){

        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    }

    /**
     * POST game/1/messages with existing game
     * Result: 201 Created
     */
    @Test
    @WithMockUser(username = "testUsername")
    void createMessageSuccess() throws Exception{
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame = gameService.createGame(testGame);
        //given
        testMessage = new Message();
        testMessage.setUsername("testUsername");
        testMessage.setText("text");
        testMessage.setTimeCreated(time);
        testMessage.setGame(testGame);
        testMessage.setMessageId(1L);

        //Message from Post
        MessagePostDTO messagePostDTO = new MessagePostDTO();
        messagePostDTO.setUsername("testUsername");
        messagePostDTO.setText("text");

        given(chatService.createMessage(eq(1L),Mockito.any())).willReturn(testMessage);

        //when
        MockHttpServletRequestBuilder postRequest = post("/games/1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(messagePostDTO))
                .characterEncoding("utf-8");

        // then
        MvcResult result = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(testMessage.getUsername())))
                .andExpect(jsonPath("$.text", is(testMessage.getText())))
                .andExpect(jsonPath("$.timeCreated", is(time.toString())))
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(201, result.getResponse().getStatus());
        //Check Correct HTTP Response Content-Type (Data Format)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        //Check Correct HTTP Response Data
        assertEquals("{\"messageId\":1,\"username\":\"testUsername\",\"text\":\"text\",\"timeCreated\":\""+time.toString()+"\"}", result.getResponse().getContentAsString());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.POST.name(), result.getRequest().getMethod());
        //Check Correct HTTP Request Data Passing
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getRequest().getContentType());
    }
    /**
     * GET game/1/messages with existing game
     * Result: 200 List of all Messages
     */
    @Test
    @WithMockUser(username = "testUsername")
    void getMessagesSuccess() throws Exception{
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame = gameService.createGame(testGame);
        //given
        testMessage = new Message();
        testMessage.setUsername("testUsername");
        testMessage.setText("text");
        testMessage.setTimeCreated(time);
        testMessage.setGame(testGame);
        testMessage.setMessageId(1L);

        List<Message> allMessages = Collections.singletonList(testMessage);

        given(chatService.getMessages(eq(1L))).willReturn(allMessages);

        //when
        MockHttpServletRequestBuilder getRequest = get("/games/1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is(testMessage.getUsername())))
                .andExpect(jsonPath("$[0].text", is(testMessage.getText())))
                .andExpect(jsonPath("$[0].timeCreated", is(time.toString())))
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(200, result.getResponse().getStatus());
        //Check Correct HTTP Response Content-Type (Data Format)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        //Check Correct HTTP Request Data Passing
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getRequest().getContentType());
    }


    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"username": "testUsername"}
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