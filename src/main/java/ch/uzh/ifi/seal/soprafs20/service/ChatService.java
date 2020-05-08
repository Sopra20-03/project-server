package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Qualifier;

public class ChatService {
    private final ChatRepository chatRepository;
    public ChatService(@Qualifier("chatRepository") ChatRepository chatRepository){
        this.chatRepository = chatRepository;
    }
}
