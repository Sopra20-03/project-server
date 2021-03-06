package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("roundRepository")
public interface RoundRepository extends JpaRepository<Round, Long> {
    List<Round> findRoundsByGame(Game game);
    Round findRoundByRoundId(Long roundId);
    Round findRoundByGameAndRoundNum(Game game, int roundNum);
    Round findRoundByGameAndRoundStatus(Game game, RoundStatus roundStatus);
}
