package ch.uzh.ifi.seal.soprafs20.service;



import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.repository.GuessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public Guess setGuess(Round round, Guess guess){
        guess.setRound(round);

        round.setGuess(guess);
        guessRepository.save(guess);
        guessRepository.flush();
        return guess;
    }

    public Guess getGuess(Round round){
        return guessRepository.getGuessByRound(round);
    }
}
