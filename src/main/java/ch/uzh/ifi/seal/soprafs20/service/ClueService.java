package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.NoClueException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.PlayerAlreadySubmittedClueException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.PlayerIsNotClueWriterException;
import ch.uzh.ifi.seal.soprafs20.exceptions.WordCard.NoWordSelectedException;
import ch.uzh.ifi.seal.soprafs20.repository.ClueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ClueService {

    private final ClueRepository clueRepository;

    @Autowired
    public ClueService(@Qualifier("clueRepository")ClueRepository clueRepository) {
        this.clueRepository = clueRepository;
    }

    public Clue setClue(Round round, RealPlayer owner, Clue clue) {

        //check if player is clue_writer
        if(owner.getRole() != Role.CLUE_WRITER) {
            throw new PlayerIsNotClueWriterException(owner.toString());
        }
        //check if player already submitted a clue
        if(clueRepository.getCluesByOwnerAndRound(owner, round).size() != 0) {
            throw new PlayerAlreadySubmittedClueException(owner.toString());
        }

        //set clue
        clue.setRound(round);
        clueRepository.save(clue);
        clueRepository.flush();
        clue.setOwner(owner);
        clue.setIsValid(true);

        clueRepository.save(clue);
        clueRepository.flush();

        return clue;
    }

    public Clue getClue(Round round) { return clueRepository.getClueByRound(round); }


    /**
     * get all Clues of a round
     * @param round
     * @return List<Clue>
     */

    public List<Clue> getClues(Round round){
        List<Clue> clues = clueRepository.getCluesByRound(round);
        if(clues.size()==0){
            throw new NoClueException(round.getRoundId().toString());
        }
        return clues;
    }

    /**
     * validates clues of a round
     * @param round
     *
     */
    public void validateClues(Round round){
        List<Clue> clues = getClues(round);
        String selectedWord = round.getWordCard().getSelectedWord();

        if(selectedWord==null){
            throw new NoWordSelectedException(round.getRoundId().toString());
        }
        for(Clue clue : clues){
            int numbOfEqualWords = 0;
            //count number of same words
            for(Clue compareClue:clues){
                if(clue.getWord().equalsIgnoreCase(compareClue.getWord())){
                    numbOfEqualWords++;
                }
            }
            //if there are more than 1 times the same word or the word is the same as the selected word, set valid to false
            if(numbOfEqualWords> 1 || clue.getWord().equalsIgnoreCase(selectedWord)){
                clue.setIsValid(false);
                clueRepository.save(clue);
                clueRepository.flush();
            }
        }
    }

    /**
     * creates an empty clue for each player and each round in a game
     * @param game
     * @return Game
     */
    public Game setEmptyClues(Game game) {
        //get list of rounds and number of players in game
        List<Round> rounds = game.getRounds();
        int numPlayers = game.getPlayerCount();

        //create new list to return clues
        List<Clue> clues = new ArrayList<>();

        //set an empty clue for each player in each round
        for(Round round: rounds) {
            for(int i = 1; i < numPlayers; i++) {
                Clue clue = new Clue();
                clue.setRound(round);
                clue.setIsValid(false);
                clues.add(clue);

                clueRepository.save(clue);
                clueRepository.flush();
            }
        }
        return game;
    }

}
