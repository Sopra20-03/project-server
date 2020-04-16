package ch.uzh.ifi.seal.soprafs20.service;



import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import ch.uzh.ifi.seal.soprafs20.exceptions.WordCard.NoWordCardException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Guess.NoGuessException;
import ch.uzh.ifi.seal.soprafs20.exceptions.WordCard.NoWordSelectedException;
import ch.uzh.ifi.seal.soprafs20.repository.GuessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GuessService {
    private final GuessRepository guessRepository;

    @Autowired
    public GuessService(@Qualifier("guessRepository") GuessRepository guessRepository){
        this.guessRepository = guessRepository;
    }

    /**
     * Sets a new Guess to a round & save it in the repository
     * @param round ,guess  Round to which to submit the guess
     * @return Guess
     */
    public Guess setGuess(Round round, Guess guess) {

        guess.setRound(round);
        //validate guess
        WordCard wordCard = round.getWordCard();
        //throw exception if WordCard is null
        if (wordCard == null) {
            throw new NoWordCardException(round.toString());
        }
        guess.setIsValid(correctGuess(wordCard, guess));
        guessRepository.save(guess);
        guessRepository.flush();

        return guess;
    }
    /**
     * Get Guess of a Round
     * @param round
     * @return Guess
     */
    public Guess getGuess(Round round){
        Guess guess = guessRepository.getGuessByRound(round);
        if(guess == null){
            throw new NoGuessException(round.toString());
        }
        return guess;
    }
    /**
     * Helper method to check if guess is similar than selected Wordcard
     * @param wordCard, guess  wordcard and Guess to compare
     * @return true if guess is correct
     */
    public boolean correctGuess(WordCard wordCard,Guess guess){
        String selectedWord = wordCard.getSelectedWord();
        String guessedWord = guess.getWord();
        if(selectedWord==null){
            throw new NoWordSelectedException(wordCard.toString());
        }
        return selectedWord.equalsIgnoreCase(guessedWord);
    }
}
