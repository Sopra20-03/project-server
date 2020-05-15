package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Synonym;
import ch.uzh.ifi.seal.soprafs20.service.ClueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BotControllerTest {
    @Autowired
    ClueService clueService;
    @Test
    public void getSimilarWordTest(){
        BotController botController = new BotController(clueService);
        List<Synonym> synonymsOfSwitzerland = botController.getSimilarWords("switzerland");

        assertEquals("chf",synonymsOfSwitzerland.get(0).getWord());

    }

    /**
     * check if synonym: diamonds get removed, because it is too similar to the selected word
     */
    @Test
    public void removeEqualWordsTest(){
        BotController botController = new BotController(clueService);
        List<Synonym> synonymsOfDiamond = botController.getSimilarWords("diamond");
        List<String> synonymsBeforeRemoving = new ArrayList<>();
        for(Synonym synonym: synonymsOfDiamond){
            synonymsBeforeRemoving.add(synonym.getWord());
        }
        synonymsOfDiamond = botController.removeEqualWords(synonymsOfDiamond,"diamond");
        List<String> synonymsAfterRemoving = new ArrayList<>();
        for(Synonym synonym: synonymsOfDiamond){
            synonymsAfterRemoving.add(synonym.getWord());
        }
        assertTrue(synonymsBeforeRemoving.contains("diamonds"));
        assertFalse(synonymsAfterRemoving.contains("diamonds"));


    }
}
