package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.ClueWithIdAlreadySubmitted;
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
import java.util.Set;

@Service
@Transactional
public class ClueService {

    private final ClueRepository clueRepository;

    @Autowired
    public ClueService(@Qualifier("clueRepository")ClueRepository clueRepository) {
        this.clueRepository = clueRepository;
    }

    //TODO: don't need this anymore??
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
        clue.setOwner(owner);
        clue.setIsValid(true);
        clue.setVotes(0);

        clueRepository.save(clue);
        clueRepository.flush();

        return clue;
    }

    /**
     * sets fields of empty clue
     * @param clue
     * @param owner
     * @param word
     * @return Clue
     */
    public Clue submitClue(Clue clue, RealPlayer owner, String word) {

        //check if player is clue_writer
        if(owner.getRole() != Role.CLUE_WRITER) {
            throw new PlayerIsNotClueWriterException(owner.toString());
        }

        //check if clue with id was already submitted
        if(clue.getWord() != null) {
            throw new ClueWithIdAlreadySubmitted(clue.getClueId().toString());
        }

        //check if player already submitted a clue
        if(clueRepository.getCluesByOwnerAndRound(owner, clue.getRound()).size() != 0) {
            throw new PlayerAlreadySubmittedClueException(owner.toString());
        }

        //set owner, word, and valid
        clue.setOwner(owner);
        clue.setWord(word);
        clue.setIsValid(true);

        return clue;
    }
    /**
     * submits a clue for a bot
     *
     * @param round
     * @param word
     * @return Clue
     */
    public Clue submitBotClue(Round round, String word) {
        Clue clue = new Clue();
        clue.setRound(round);

        //set owner, word, and valid
        clue.setVotes(0);
        clue.setWord(word);
        clue.setIsValid(true);
        clueRepository.save(clue);
        clueRepository.flush();
        return clue;
    }

    /**
     * gets clue by clueId
     * @param clueId
     * @return Clue
     */
    public Clue getClue(Long clueId) { return clueRepository.getClueByClueId(clueId); }


    public Clue getClueById(long clueId){return clueRepository.getClueByClueId(clueId);}

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
     */
    public void validateClues(Round round){
        List<Clue> clues = getClues(round);
        String selectedWord = round.getWordCard().getSelectedWord();

        if(selectedWord==null){
            throw new NoWordSelectedException(round.getRoundId().toString());
        }

        for(Clue clue : clues){
            /*
            int numbOfEqualWords = 0;
            //count number of same words
            for(Clue compareClue:clues){
                if(clue.getWord().equalsIgnoreCase(compareClue.getWord())){
                    numbOfEqualWords++;
                }
            }
            //if there are more than 1 times the same word or the word is the same as the selected word, set valid to false
            if(numbOfEqualWords> 1 || clue.getWord().equalsIgnoreCase(selectedWord) ){
                clue.setIsValid(false);

            }

             */
            //validate clue according to votes
            if(clue.getVotes() < 0){
                clue.setIsValid(false);
            }
            else if(clue.getVotes() >= 0){
                clue.setIsValid(true);
            }
            clueRepository.save(clue);
            clueRepository.flush();
            }

    }

    public Clue manuallyValidateClues(Clue clue, Vote vote) {
        int currentVotes = clue.getVotes();
        if(vote.getVote()){
            clue.setVotes(currentVotes+1);
        }
        else {
            clue.setVotes(currentVotes-1);
        }
        Round round = clue.getRound();
        validateClues(round);
        //save changes
        clueRepository.save(clue);
        clueRepository.flush();
        return clue;
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
        Set<RealPlayer> players = game.getPlayers();

        //create new list to return clues
        List<Clue> clues = new ArrayList<>();

        //set an empty clue for each clue writer in each round
        for(Round round: rounds) {
            for(RealPlayer player : players) {

                if(player.getRole()==Role.CLUE_WRITER) {

                    Clue clue = new Clue();
                    clue.setRound(round);
                    clue.setOwnerId(player.getPlayerId());
                    clue.setIsValid(false);

                  
                    clue.setVotes(0);
                    clues.add(clue);


                    clueRepository.save(clue);
                    clueRepository.flush();
                }
            }
        }
        return game;
    }

}
