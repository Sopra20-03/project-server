package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Synonym;
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
    }

