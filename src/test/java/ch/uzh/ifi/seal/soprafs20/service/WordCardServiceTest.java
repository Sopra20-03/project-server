package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class WordCardServiceTest {

    @Autowired
    private WordCardService wordCardService;

    @MockBean
    private WordCard wordCard1;
    private WordCard wordCard2;

    @BeforeEach
    public void setup() {
        //add 2 wordCards
        wordCardService.addAllWordCards();
    }

    @Test
    void getShuffledWordCards() {
        List<WordCard> orderedCards = wordCardService.getWordCards();
        List<WordCard> shuffledCards = wordCardService.getShuffledWordCards();

        assertEquals(orderedCards.size(), shuffledCards.size());

        for(WordCard card : orderedCards) {
            assertTrue(shuffledCards.contains(card));
        }
    }
    @Test
    public void whenReadWithScanner_thenCorrect()
            throws IOException {
        String file = "src/main/resources/fileTest.txt";
        Scanner scanner = new Scanner(new File(file));
        scanner.useDelimiter(" ");

        assertTrue(scanner.hasNext());
        assertEquals("Hello,", scanner.next());
        assertEquals("world!", scanner.next());

        scanner.close();
    }
}
