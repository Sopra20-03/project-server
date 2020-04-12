package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import ch.uzh.ifi.seal.soprafs20.exceptions.Round.RoundNotFoundException;
import ch.uzh.ifi.seal.soprafs20.repository.RoundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Round Service
 * This class is the "worker" and responsible for all functionality related to the rounds
 * (e.g., it creates, validateClues, start, end validateGuess). The result will be passed back to the caller.
 */
@Service
@Transactional
public class RoundService {
    private final Logger log = LoggerFactory.getLogger(RoundService.class);

    private final RoundRepository roundRepository;
    private final int NUMBER_OF_ROUNDS = 2;

    @Autowired
    public RoundService(@Qualifier("roundRepository") RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    /**
     * Creates 12 rounds of a game & save it into table T_ROUNDS
     * @param game creates rounds for game with gameId
     */
    public Game createRounds(Game game, List<WordCard> cards){

        for(int roundNum = 1; roundNum <= NUMBER_OF_ROUNDS; roundNum++){
            //create round
            Round round = new Round();
            round.setGame(game);
            round.setRoundNum(roundNum);
            round.setWordCard(cards.get(roundNum));
            //save round
            roundRepository.save(round);
            roundRepository.flush();
            //add round to game


            log.debug("Created Round: {}", round);
        }
        return game;
    }
    /**
     * Gets all rounds stored in T_ROUNDS
     * @return List<Round>
     */
    public List <Round> getRounds(){
        return this.roundRepository.findAll();
    }

    /**
     * Returns all Rounds of a given gameId
     * @param game of the game
     * @return List<Round>
     */
    public List<Round> getRoundsOfGame(Game game){
        return roundRepository.findRoundsByGame(game);
    }

    /**
     * Gets a specific round in a specific game
     * @param game
     * @param roundId
     * @return Round
     */
    public Round getRoundById(Game game, long roundId) {
        List<Round> roundsOfGame = getRoundsOfGame(game);
        Round correctRound = new Round();

        for( Round round : roundsOfGame) {
            if(round.getRoundId() == roundId) { correctRound = round; }
        }
        if(correctRound.getRoundId() != roundId) { throw new RoundNotFoundException(); }

        return correctRound;
    }
}
