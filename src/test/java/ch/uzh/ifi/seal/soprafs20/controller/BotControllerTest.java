package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Synonym;
import ch.uzh.ifi.seal.soprafs20.service.ClueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BotControllerTest {
    @Autowired
    ClueService clueService;
    @Test
    public void getSimilarWordTest(){
        BotController botController = new BotController(clueService);
        List<Synonym> synonymsOfSwitzerland = botController.getSimilarWords("switzerland");

        assertEquals("chf",synonymsOfSwitzerland.get(0).getWord());



    }
}
