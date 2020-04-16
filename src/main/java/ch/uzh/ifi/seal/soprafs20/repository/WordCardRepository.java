package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("wordCardRepository")
public interface WordCardRepository extends JpaRepository<WordCard, Long> {
    WordCard getWordCardByRound(Round round);
}
