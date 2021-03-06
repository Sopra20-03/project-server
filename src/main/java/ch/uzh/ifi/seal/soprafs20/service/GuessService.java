package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import ch.uzh.ifi.seal.soprafs20.exceptions.Guess.NoGuessException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Guess.RoundHasAlreadyGuessException;
import ch.uzh.ifi.seal.soprafs20.exceptions.WordCard.NoWordSelectedException;
import ch.uzh.ifi.seal.soprafs20.repository.GuessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
    if(guessRepository.getGuessByRound(round) != null ){
        throw new RoundHasAlreadyGuessException("Round with Round Number: "+ round.getRoundNum());
    }
        guess.setRound(round);
        //validate guess
        WordCard wordCard = round.getWordCard();

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
            throw new NoGuessException("Round Number: " + round.getRoundNum());
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
            throw new NoWordSelectedException("WordCardId: " + wordCard.getWordCardId());
        }
        return selectedWord.equalsIgnoreCase(guessedWord);
    }

    /**
     * sets startTime for Guess in round
     * @param round
     */
    public void setStartTime(Round round) {

        //exception if no guess was submitted
        if(guessRepository.getGuessByRound(round) == null) {
            throw new NoGuessException(round.getRoundId().toString());
        }

        //get guess in round
        Guess guess = guessRepository.getGuessByRound(round);

        //get current timestamp
        LocalDateTime startTime = LocalDateTime.now();

        //set startTime for guess in round
        guess.setStartTime(startTime);
    }

    /**
     * sets endTime and calculate totalTime in seconds for Guess
     * @param guess
     */
    public void setEndTime(Guess guess) {
        //get current timestamp
        LocalDateTime endTime = LocalDateTime.now();

        //set endTime for guess
        guess.setEndTime(endTime);

        //calculate totalTime
        long totalTime = ChronoUnit.SECONDS.between(guess.getStartTime(), endTime);

        //set totalTime
        guess.setTotalTime(totalTime);
    }

    /**
     * calculates individual score for a guess
     * @param guess
     * @return
     */
    public int calculateIndividualScore(Guess guess) {

        int score;

        //20 points if guess is correct; else 0
        if(guess.getIsValid()) {
            score = 20;
        }
        else { score = 0; }

        //store score in guess
        guess.setScore(score);
        guessRepository.save(guess);
        guessRepository.flush();
        return score;
    }

}
