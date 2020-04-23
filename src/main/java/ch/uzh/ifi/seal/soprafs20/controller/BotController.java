package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Synonym;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class BotController {

    public Synonym getSimilarWord(String word){
        RestTemplate restTemplate = new RestTemplate();
        Synonym synonym = restTemplate.getForObject("https://wordsapiv1.p.mashape.com/words/"+word, Synonym.class);
        return synonym;
    }
}
