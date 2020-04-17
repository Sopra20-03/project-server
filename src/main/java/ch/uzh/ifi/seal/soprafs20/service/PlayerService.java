package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.User;
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
    /**
     * Persists a player into table T_PLAYERS and add it to game
     * (works only on RealPlayers atm)
     * @param player to be persisted as a player
     * @param game the user wants to join
     * @param user the User to join a game
     * @return Player
     */
    public Game addPlayer(Game game, RealPlayer player, User user) {

        //exception if game already has five players
        if(game.getPlayers().size() >= 5) {
            throw new GameFullException("Game already has five players.");
        }

        //exception if player is already in a game
        List<RealPlayer> playersOfUser = playerRepository.findRealPlayersByUserId(player.getUserId());
        //check if one of the players of this user is in an game with status != finished
        while(playersOfUser.iterator().hasNext()){
            Game gameOfPlayer = playersOfUser.iterator().next().getGame();
            if(gameOfPlayer.getGameStatus() != GameStatus.FINISHED) {
                throw new PlayerAlreadyInGameException(" User with UserId: " + player.getUserId().toString()+" is in Game with GameId: "+gameOfPlayer.getGameId());
            }
        }

        player.setGame(game);
        player.setUserName(user.getUsername());
        playerRepository.save(player);
        playerRepository.flush();
        log.debug("Added player: {} to game: {}", player, game);
        return game;
    }

}
