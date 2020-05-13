package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import ch.uzh.ifi.seal.soprafs20.repository.WordCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class WordCardService {
    private final Logger log = LoggerFactory.getLogger(RoundService.class);

    private final WordCardRepository wordCardRepository;

    @Autowired
    public WordCardService(@Qualifier("wordCardRepository") WordCardRepository wordCardRepository) {
        this.wordCardRepository = wordCardRepository;
    }



    /**
     * Gets all wordCards stored in T_WordCards
     * @return List<WordCard>
     */
    public List<WordCard> getWordCards() {
        return this.wordCardRepository.findAll();
    }

    /**
     * Gets wordCard of a Round stored in T_WordCards
     * @return WordCard
     */
    public WordCard getWordCard(Round round) {
        return this.wordCardRepository.getWordCardByRound(round);
    }

    /**
     * adds selectedWord in T_WordCards
     * @param round
     * @param selectedWord
     */
    public void selectWord(Round round, String selectedWord) {
        WordCard wordCard = round.getWordCard();
        wordCard.setSelectedWord(selectedWord);
    }

    /**
     * sets selectedWord in T_WordCards
     * @param round, selectedWord
     * @return Round
     */
    public Round setSelectedWord(Round round, String selectedWord) {
        WordCard wordCard = round.getWordCard();
        wordCard.setSelectedWord(selectedWord);

        return round;
    }

    /**
     * creates a WordCard in T_WordCards
     * @param word1
     * @param word2
     * @param word3
     * @param word4
     * @param word5
     * @return wordCard
     */
    public WordCard createWordCard(String word1, String word2, String word3, String word4, String word5) {
        WordCard wordCard = new WordCard();
        wordCard.setWord1(word1);
        wordCard.setWord2(word2);
        wordCard.setWord3(word3);
        wordCard.setWord4(word4);
        wordCard.setWord5(word5);

        wordCardRepository.save(wordCard);
        wordCardRepository.flush();

        return wordCard;
    }
    /**
     * creates numberOfCards times a new Card from cards-EN.txt file
     * @param numberOfCards
     * @return List<WordCard>
     */
    public List<WordCard> getWordCards(int numberOfCards) {
        List<WordCard> wordCards = new ArrayList<>();
        //read words into array
        Scanner scanner = null;
        try {
            String file = "src/main/resources/cards-EN.txt";
            scanner = new Scanner(new File(file));

            List<String> words = new ArrayList<>();
            while (scanner.hasNext()) {
                words.add(scanner.next());
            }
/*
        //TODO: shuffle Removed for Testing
        Collections.shuffle(words);
*/

            //read every 4 word out of array and create a WordCard
            for (int i = 0; i < 5 * numberOfCards; i = i + 5) {
                WordCard wordCard = createWordCard(words.get(i), words.get(i + 1), words.get(i + 2), words.get(i + 3), words.get(i + 4));
                wordCards.add(wordCard);
            }
            scanner.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("FileNotFoundException thrown", e);
        }
        finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return wordCards;

    }
}
