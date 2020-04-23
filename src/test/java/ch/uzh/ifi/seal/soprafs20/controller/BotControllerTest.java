package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Synonym;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BotControllerTest {
    @Test
    public void getSimilarWordTest(){
        BotController botController = new BotController();
        Synonym synonymOfDwarf = botController.getSimilarWord("dwarf");
        assertEquals("dwarf",synonymOfDwarf.getWord());
        List<String> trueSynonyms = new ArrayList<>(List.of("midget","nanus","gnome","overshadow","shadow"));
        assertEquals(trueSynonyms, synonymOfDwarf.getSynonyms());
    }
}
