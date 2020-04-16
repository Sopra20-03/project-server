package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.GameFullException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.PlayerAlreadyInGameException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.PlayerNotInGameException;
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


    public RealPlayer getPlayer(Long id) {
        return playerRepository.findRealPlayerByUserId(id);
    }

    /**
     * Persists a player into table T_PLAYERS
     * (works only on RealPlayers atm)
     * @param player to be persisted as a player
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


    public void removePlayer( Game game, Long userId){

        List<RealPlayer> players = playerRepository.findRealPlayersByGameAndUserId(game,userId);

        //exception if player is not in the game
        if(players.size() == 0) {
            throw new PlayerNotInGameException("UserId: " + userId.toString());
        }
        for(RealPlayer player:players) {
            playerRepository.delete(player);
        }
    }
    public Game addPlayer(Game game, RealPlayer player) {

        //exception if game already has five players
        if(game.getPlayers().size() >= 5) {
            throw new GameFullException(" : Game already has five players.");
        }

        //exception if player is already in the game

        if(playerRepository.findRealPlayerByUserId(player.getUserId())!= null){
            throw new PlayerAlreadyInGameException(" UserId: " + player.getUserId().toString()+" ");
        }

        player.setGame(game);
        playerRepository.save(player);
        playerRepository.flush();
        log.debug("Added player: {} to game: {}", player, game);
        return game;
    }

}
