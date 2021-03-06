package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
public class WordCardServiceTest {

    @Autowired
    private WordCardService wordCardService;

    @MockBean
    private WordCard wordCard1;
    private WordCard wordCard2;

    @BeforeEach
    public void setup() throws IOException {
        //add 2 wordCards
    }

    @Test
    public void whenReadWithScanner_thenCorrect()
            throws IOException {
        String file = "src/main/resources/fileTest.txt";
        Scanner scanner = new Scanner(new File(file));
        List<String> words = new ArrayList<>();
        while(scanner.hasNext()){
            words.add(scanner.next());
        }
        scanner.close();
        assertEquals(5,words.size());
        int numberOfWordCards = words.size()/4;
        assertEquals(1,numberOfWordCards);
    }

    @Test
    void wordCardImport(){
        //test if first card is correct
        wordCardService.getWordCards(12);
        assertEquals("Alcatraz",wordCardService.getWordCards().get(0).getWord1());
    }
}
