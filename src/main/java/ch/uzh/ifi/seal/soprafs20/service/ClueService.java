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

    public Clue manuallyValidateClues(Game game, Long clueId, boolean vote) {
        Clue clue = clueRepository.getClueByClueId(clueId);
        clue.addVote(vote);
        List<Boolean> votes = clue.getVotes();

        int numValidations = game.getPlayerCount() - 2;
        if(numValidations > 0) {
            int positive = 0;
            int negative = 0;
            for(boolean validation : votes) {
                if(validation) { positive++; }
                if(!validation) {negative++; }
            }
            if(positive > negative) { clue.setIsValid(true); }
            if(positive < negative) { clue.setIsValid(false); }
        }
        return clue;
    }

}
