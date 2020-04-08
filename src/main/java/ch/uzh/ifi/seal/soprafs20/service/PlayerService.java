package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final PlayerRepository playerRepository;


    @Autowired
    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<RealPlayer> getPlayersByGame(Game game) {
        return playerRepository.findRealPlayersByGame(game);
    }

    /**
     * Persists a player into table T_PLAYERS
     * (works only on RealPlayers atm)
     * @param user to be persisted as a player
     * @param game the user wants to join
     * @return Player
     */
    public RealPlayer createPlayer(RealPlayer player, Game game) {

        //CompleteDetails
        player.setGame(game);

        // saves the given entity but data is only persisted in the database once flush() is called
        playerRepository.save(player);
        playerRepository.flush();

        log.debug("Created Information for Player: {}", player);
        return player;
    }
}
