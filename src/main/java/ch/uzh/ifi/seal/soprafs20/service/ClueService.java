package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.WordCard.NoWordSelectedException;
import ch.uzh.ifi.seal.soprafs20.repository.ClueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    /**
     * create a new clue
     * @param round
     * @param owner
     * @param clue
     * @return
     */
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
    public Clue getClueById(long clueId){
        Clue clue = clueRepository.getClueByClueId(clueId);
        if(clue == null){
            throw new ClueNotFoundException("Clue Id:"+clueId);
        }
        return clue;}

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
     * invalidates clues if string or substring is equal
     * @param round
     * @return
     */
    public void autoValidateClues(Round round) {
        List<Clue> clues = getClues(round);
        String selectedWord = round.getWordCard().getSelectedWord();

        //exception if no word is selected yet for the round
        if (selectedWord == null) {
            throw new NoWordSelectedException(round.getRoundId().toString());
        }

        if(clues.isEmpty()) {
            throw new NoClueException(round.getRoundId().toString());
        }

        //count number of same words and substrings
        for (Clue clue : clues) {
            int numbOfEqualWords = 0;
            int numbOfSubstringWords = 0;
            List<Integer> substrings = new ArrayList<>();
            for (Clue compareClue : clues) {
                if (clue.getWord() != null && compareClue.getWord() != null && clue.getWord().equalsIgnoreCase(compareClue.getWord())) {
                    numbOfEqualWords++;
                }
                if (clue.getWord() != null && compareClue.getWord() != null && clue.getWord().toLowerCase().contains(compareClue.getWord().toLowerCase())) {
                    numbOfSubstringWords++;
                    substrings.add(clues.indexOf(compareClue));
                }
            }
            //if there are more than 1 times the same word or the word is the same as the selected word, set valid to false
            if (numbOfEqualWords > 1 || (clue.getWord() != null && clue.getWord().equalsIgnoreCase(selectedWord))) {
                clue.setIsValid(false);
                clueRepository.save(clue);
                clueRepository.flush();
            }
            if(numbOfSubstringWords > 1) {
                clue.setIsValid(false);
                clueRepository.save(clue);
                clueRepository.flush();
                for(Integer substring : substrings) {
                    clues.get(substring).setIsValid(false);
                    clueRepository.save(clues.get(substring));
                    clueRepository.flush();
                }
            }
        }
    }

    /**
     * creates an empty clue for each clue writer in a round
     * @param game
     * @return Game
     */
    public Game setEmptyClues(Game game, Round round) {
        //get list of players and number of players in game
        int numPlayers = game.getPlayerCount();
        List<RealPlayer> players = game.getPlayers();

        //create new list to return clues
        List<Clue> clues = new ArrayList<>();

        //set an empty clue for each clue writer in each round
        for(RealPlayer player : players) {
            if(player.getRole() == Role.CLUE_WRITER) {
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
        return game;
    }

    /**
     * sets startTime for all Clues in round
     * @param round
     */
    public void setStartTime(Round round) {


        if(clueRepository.getCluesByRound(round).isEmpty()) {
            throw new NoClueException(round.getRoundId().toString());
        }

        //get all clues in round
        List<Clue> clues = clueRepository.getCluesByRound(round);

        //get current timestamp
        LocalDateTime startTime = LocalDateTime.now();

        //set startTime for all clues in round
        for(Clue clue : clues) {
            clue.setStartTime(startTime);
        }
    }

    /**
     * sets endTime and calculate totalTime in seconds for Clue
     * @param clue
     */
    public void setEndTime(Clue clue) {
        //get current timestamp
        LocalDateTime endTime = LocalDateTime.now();

        //set endTime for clue
        clue.setEndTime(endTime);

        //calculate totalTime
        long totalTime = ChronoUnit.SECONDS.between(clue.getStartTime(), endTime);

        //set totalTime
        clue.setTotalTime(totalTime);
    }

    /**
     * calculates individual score for a clue
     * @param round
     * @param clue
     * @return
     */
    public int calculateIndividualScore(Round round, Clue clue) {

        //get clues of round in ascending order by total time
        List<Clue> clues = clueRepository.findAllByRoundOrderByTotalTimeAsc(round);

        if(!clues.contains(clue)) {
            throw new ClueNotInRoundException(clue.getClueId().toString(), round.getRoundId().toString());
        }

        //calculate score depending on how many clues were already submitted
        int clueCount = clues.size();
        int score = 10 - (clueCount-1)*2;

        return score;
    }

}
