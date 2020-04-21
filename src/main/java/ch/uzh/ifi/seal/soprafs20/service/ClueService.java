package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.PlayerAlreadySubmittedClueException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.PlayerIsNotClueWriterException;
import ch.uzh.ifi.seal.soprafs20.repository.ClueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        if(clueRepository.getClueByOwnerAndRound(owner, round) != null) {
            throw new PlayerAlreadySubmittedClueException(owner.toString());
        }

        //set clue
        clue.setRound(round);
        clue.setOwner(owner);

        clueRepository.save(clue);
        clueRepository.flush();

        return clue;
    }

    public Clue getClue(Round round) { return clueRepository.getClueByRound(round); }
}
