package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final GameService gameService;
    @Autowired
    public ChatService(@Qualifier("chatRepository") ChatRepository chatRepository, GameService gameService){
        this.chatRepository = chatRepository;
        this.gameService = gameService;
    }
    /**
     * Persists a message, and connect it to a game with gameId
     * @param message to be persisted
     * @return Message
     */
    public Message createMessage(long gameId, Message message){
        //set game
        Game game = gameService.getGame(gameId);
        message.setGame(game);
        //set time Created
        message.setTimeCreated(LocalDateTime.now());
        chatRepository.save(message);
        chatRepository.flush();
        return message;
    }

    /**
     * get all messages from a game
     * @param gameId
     * @return List of Messages
     */
    public List<Message> getMessages(long gameId){
        return chatRepository.getMessagesByGame_GameId(gameId);
    }
}
