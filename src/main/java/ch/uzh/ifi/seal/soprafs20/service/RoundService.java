package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import ch.uzh.ifi.seal.soprafs20.exceptions.Round.NoRunningRoundException;
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
    private final int NUMBER_OF_ROUNDS = 3;

    @Autowired
    public RoundService(@Qualifier("roundRepository") RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    /**
     * Creates 13 rounds of a game & save it into table T_ROUNDS
     * @param game creates rounds for game with gameId
     */
    public Game createRounds(Game game, List<WordCard> cards){

        for(int roundNum = 1; roundNum <= NUMBER_OF_ROUNDS; roundNum++){
            //create round
            Round round = new Round();
            round.setGame(game);
            round.setRoundNum(roundNum);

            round.setRoundStatus(RoundStatus.INITIALIZED);
            round.setWordCard(cards.listIterator().next());

            //save round
            roundRepository.save(round);
            roundRepository.flush();
            //add round to game


            log.debug("Created Round: {}", round);
        }
        return game;
    }

    public Game removeRounds(Game game){

        // get list of rounds
        List<Round> rounds = roundRepository.findRoundsByGame(game);

        for(Round round : rounds){

            // round
            roundRepository.delete(round);
            roundRepository.flush();

            log.debug("Deleted Round: {}", round);
        }
        return game;
    }

    /**
     * Start first Round
     * @param game
     * @return started Game
     */
    public Game startFirstRound(Game game){
        Round round = roundRepository.findRoundByGameAndRoundNum(game,1);
        round.setRoundStatus(RoundStatus.RUNNING);
        roundRepository.save(round);
        roundRepository.flush();
        return game;
    }
    /**
     * Start next Round
     * @param game
     * @return started Game
     */
    public Game startNextRound(Game game){
        Round round = getRunningRound(game);
        int RoundNum = round.getRoundNum();
        //if there is a next round, set it to running
        if (RoundNum < NUMBER_OF_ROUNDS){
            Round nextRound = roundRepository.findRoundByGameAndRoundNum(game,RoundNum+1);
            nextRound.setRoundStatus(RoundStatus.RUNNING);
            roundRepository.save(nextRound);
            roundRepository.flush();

            //set new Roles of players
            List<RealPlayer> players = game.getPlayers();
            //ListIterator<RealPlayer> previousGuesserIt = players.listIterator();

            //get index of current guesser and calculate index of next guesser
            int currentGuesserIndex = 0;
            int nextGuesserIndex;
            for(RealPlayer player : players) {
                if(player.getRole() == Role.GUESSER) {
                    currentGuesserIndex = players.indexOf(player);
                }
            }

            if(currentGuesserIndex == players.size()-1) {
                nextGuesserIndex = 0;
            }

            else { nextGuesserIndex = currentGuesserIndex+1; }

            //set all players to clue_writers
            for(RealPlayer player : players) {
                player.setRole(Role.CLUE_WRITER);
            }

            //set next guesser to guesser
            players.get(nextGuesserIndex).setRole(Role.GUESSER);


        }
        round.setRoundStatus(RoundStatus.FINISHED);
        roundRepository.save(round);
        roundRepository.flush();
        return game;
    }

    /**
     * get running Round
     * @param game
     * @return Round
     */
    public Round getRunningRound(Game game){
        Round round =  roundRepository.findRoundByGameAndRoundStatus(game,RoundStatus.RUNNING);
        if (round == null){
            throw new NoRunningRoundException("Game with GameId: " + game.getGameId());
        }
        return round;
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
     * @param roundNum
     * @return Round
     */
    public Round getRoundByRoundNum(Game game, int roundNum) {
        Round round = roundRepository.findRoundByGameAndRoundNum(game,roundNum);
        if (round == null) {
            throw new RoundNotFoundException("Round with RoundNum: " + roundNum);
        }

        return round;

    }

    /**
     * true if last round is finished
     * @param game
     */
    public boolean lastRoundFinished(Game game){
        Round round = roundRepository.findRoundByGameAndRoundNum(game,NUMBER_OF_ROUNDS);
        return round.getRoundStatus()==RoundStatus.FINISHED;
    }

    /**
     * true if its last round
     * @param round
     */
    public boolean isLastRound(Round round){
        return round.getRoundNum() == NUMBER_OF_ROUNDS;
    }

    /**
     * finishes round
     * @param round
     */
    public Round finishRound(Round round){
        round.setRoundStatus(RoundStatus.FINISHED);
        roundRepository.save(round);
        roundRepository.flush();
        return round;
    }
}
