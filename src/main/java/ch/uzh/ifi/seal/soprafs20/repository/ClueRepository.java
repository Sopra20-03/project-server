package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("clueRepository")
public interface ClueRepository extends JpaRepository<Clue, Long> {
    Clue getClueByRound(Round round);
    Clue getClueByOwner(RealPlayer owner);
}
