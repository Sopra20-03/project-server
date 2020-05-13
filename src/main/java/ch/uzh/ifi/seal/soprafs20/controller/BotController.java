package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.BotMode;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.entity.Synonym;
import ch.uzh.ifi.seal.soprafs20.service.ClueService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BotController {

    private final ClueService clueService;

    public BotController(ClueService clueService){
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


    /**
     * creates 5 - numberOfRealPlayers many clues from bot
     * @param round
     * @return
     */
    public Round submitClues(Round round){
        //get game and selected word
        Game game = round.getGame();
        String word = round.getWordCard().getSelectedWord();
        //get word that is not selected to use for malicious bot
        String notWord;
        if(round.getWordCard().getWord5().equals(word)) { notWord = round.getWordCard().getWord3(); }
        else { notWord = round.getWordCard().getWord5(); }

        //if bot is malicious: create clues with similar words for not selected word
        if(game.getBotMode() == BotMode.MALICIOUS) {
            //get synonyms with not the selected word
            List<Synonym> notSynonyms = getSimilarWords(notWord);
            int numbOfClues = 5- round.getGame().getPlayerCount();
            for (int i = 0; i < numbOfClues; i++) {
                //Collections.shuffle(synonyms);
                clueService.submitBotClue(round,notSynonyms.get(i).getWord());
            }
        }

        //if bot is friendly: create clues with similar words for selected word
        else {
            List<Synonym> synonyms = getSimilarWords(word);
            int numbOfClues = 5- round.getGame().getPlayerCount();
            for (int i = 0; i < numbOfClues; i++) {
                //Collections.shuffle(synonyms);
                clueService.submitBotClue(round,synonyms.get(i).getWord());
            }
        }

        return round;
    }
    }

