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

import java.util.Collections;
import java.util.List;

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
     * @return List<Round>
     */
    public List<WordCard> getShuffleddWordCards() {
        List<WordCard> cards = this.wordCardRepository.findAll();
        Collections.shuffle(cards);
        return cards;
    }

    /**
     * adds selectedWord in T_WordCards
     * @param round
     * @param selectedWord
     */
    public void selectWord(Round round, String selectedWord) {
        WordCard wordCard = round.getCard();
        wordCard.setSelectedWord(selectedWord);
    }
}
