package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
class ChatServiceTest {

    @Autowired
    private ChatService chatService;
    @Autowired
    private GameService gameService;

    private Game testGame;
    private Message testMessage;
    private Message testMessage2;
    private List<Message> messages;

    @BeforeEach
    void setup() {
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame = gameService.createGame(testGame);
    }

    @Test
    void createMessage() {
        testMessage = new Message();
        testMessage.setText("text");
        testMessage.setUsername("testUser");
        chatService.createMessage(1L,testMessage);
        //check if time created is added
        assertNotNull(testMessage.getTimeCreated());
        //check if game is connected to message
        assertEquals(testGame.getGameId(),testMessage.getGame().getGameId());
    }
    @Test
    void getMessages(){
        testMessage = new Message();
        testMessage.setText("text");
        testMessage.setUsername("testUser");
        chatService.createMessage(1L,testMessage);
        //check if message is stored in db
        messages = chatService.getMessages(1L);
        assertEquals(1,messages.size());
    }

    /**
     * assert that no error is throw if no Messages exist for a game
     */
    @Test
    void getMessagesEmpty(){
        messages = chatService.getMessages(1L);
        assertEquals(0, messages.size());
    }
    /**
     * check if getMessages is ordered by dateCreated in ascending order
     */
    @Test
    void checkOrderGetMessages(){
        //create first message
        testMessage = new Message();
        testMessage.setText("text");
        testMessage.setUsername("testUser");
        testMessage.setMessageId(1L);
        chatService.createMessage(1L,testMessage);
        //create second message
        testMessage2 = new Message();
        testMessage2.setText("text2");
        testMessage2.setUsername("testUser2");
        testMessage.setMessageId(2L);
        chatService.createMessage(1L,testMessage2);
        //check if message is stored in db
        messages = chatService.getMessages(1L);
        assertEquals(2,messages.size());
    }
}