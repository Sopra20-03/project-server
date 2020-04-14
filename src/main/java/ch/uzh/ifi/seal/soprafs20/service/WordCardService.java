package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
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
     * Gets all wordCards stored in T_WordCards after shuffling them
     * @return List<WordCard>
     */
    public List<WordCard> getShuffledWordCards() {
        List<WordCard> cards = this.wordCardRepository.findAll();
        Collections.shuffle(cards);
        return cards;
    }

    /**
     * Gets all wordCards stored in T_WordCards
     * @return List<WordCard>
     */
    public List<WordCard> getWordCards() {
        return this.wordCardRepository.findAll();
    }

    /**
     * adds a WordCard to each Round
     * @param game
     * @return Game
     */
    public Game addWordCardsToRounds(Game game) {
        List<Round> rounds = game.getRounds();
        addAllWordCards();
        List<WordCard> cards = getShuffledWordCards();

        for(int i = 0; i < rounds.size(); i++) {
            Round round = rounds.get(i);
            round.setWordCard(cards.get(i));
        }
        return game;
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

    public void addAllWordCards() {
        createWordCard("Alcatraz",
                "Smoke",
                "Hazelnut",
                "Diamond",
                "Rose");
        createWordCard("Puppet",
                "Game",
                "Vegas",
                "Chest",
                "Airplane");
    }
}
