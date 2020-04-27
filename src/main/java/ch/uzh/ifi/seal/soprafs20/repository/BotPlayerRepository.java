package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.BotPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BotPlayerRepository extends JpaRepository<BotPlayer,Long> {
    List<BotPlayer> findBotPlayersByGame(Game game);
    BotPlayer findBotPlayerByPlayerId(Long id);
}
