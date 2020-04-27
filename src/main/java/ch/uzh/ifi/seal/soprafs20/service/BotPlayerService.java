package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.entity.*;

import ch.uzh.ifi.seal.soprafs20.repository.BotPlayerRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class BotPlayerService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final BotPlayerRepository botPlayerRepository;


    @Autowired
    public BotPlayerService(@Qualifier("botPlayerRepository") BotPlayerRepository botPlayerRepository) {
        this.botPlayerRepository = botPlayerRepository;
    }

    public List<BotPlayer> getPlayersByGame(Game game) {
        return botPlayerRepository.findBotPlayersByGame(game);
    }


    public BotPlayer getPlayerByPlayerId(Long playerId) {
        return botPlayerRepository.findBotPlayerByPlayerId(playerId);
    }

    /**
     * Creates new bot and add it to the game
     * @param game
     * @return Bot
     */
    public BotPlayer createPlayer(Game game) {
        BotPlayer bot = new BotPlayer();
        //CompleteDetails
        bot.setGame(game);
        //set name to "Bot+ unique integer"
        Date date = new Date();
        bot.setUserName("Bot"+date.hashCode());
        bot.setRole(Role.CLUE_WRITER);

        // saves the bot
        botPlayerRepository.save(bot);
        botPlayerRepository.flush();

        return bot;
    }

}
