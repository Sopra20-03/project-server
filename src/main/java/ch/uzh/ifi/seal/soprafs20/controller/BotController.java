package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.BotPlayerService;
import ch.uzh.ifi.seal.soprafs20.service.ClueService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
public class BotController {
    private final BotPlayerService botPlayerService;
    private final ClueService clueService;

    public BotController(BotPlayerService botPlayerService, ClueService clueService){
        this.botPlayerService = botPlayerService;
        this.clueService = clueService;
    }
    /**
     * gets a word and returns a list of Synonyms ordered to score (quality of synonym)
     * @param word
     * @return List of Synonyms
     */
    public List<Synonym> getSimilarWords(String word) {

        RestTemplate restTemplate = new RestTemplate();
        List<Synonym> synonyms = new ArrayList<>();

        try {
            //get words that are statistically associated with the query word in the same piece of text.
            ResponseEntity<String> response = restTemplate.getForEntity("https://api.datamuse.com/words?rel_trg=" + word, String.class);
            ObjectMapper mapper = new ObjectMapper();
            String json = response.getBody();
            synonyms = mapper.reader()
                    .forType(new TypeReference<List<Synonym>>() {
                    })
                    .readValue(json);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return synonyms;
    }
    public Game createBots(Game game) {
        int botsToCreate = 5 - game.getPlayerCount();
        for (int i = 1; i <= botsToCreate; i++) {
            botPlayerService.createPlayer(game);
        }
        return game;
    }

    /**
     * creates 5 - numberOfRealPlayers many clues from bot
     * @param round
     * @return
     */
    public Round submitClues(Round round){
        String word = round.getWordCard().getSelectedWord();
        List<Synonym> synonyms = getSimilarWords(word);
        int numbOfClues = 5- round.getGame().getPlayerCount();
        for (int i = 1; i <= numbOfClues; i++) {
            //Collections.shuffle(synonyms);
            clueService.submitBotClue(round,synonyms.get(i).getWord());
        }

        return round;
    }
    }

