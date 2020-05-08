package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Message.MessageGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Message.MessagePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {
    private final ChatService chatService;
    ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @PostMapping("/games/{gameId}/message")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MessageGetDTO createMessage(@PathVariable long gameId, @RequestBody MessagePostDTO messagePostDTO){
        Message message = DTOMapper.INSTANCE.convertMessagePostDTOtoMessageEntity(messagePostDTO);
        message = chatService.createMessage(gameId,message);
        return DTOMapper.INSTANCE.convertMessageEntityToMessageGetDTO(message);

    }
}
