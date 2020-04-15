package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.entity.Game;

import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Game Service
 * This class is the "worker" and responsible for all functionality related to the game
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class GameService {
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;


    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Returns a list of all games from table T_GAMES
     * @return List<Game>
     */
    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }

    /**
     * Returns game with given id from table "T_GAMES"
     * @param id of the game to be returned
     * @return Game
     */
    public Game getGame(Long id){
        Game game = gameRepository.findGameByGameId(id);

        if(game == null)
            throw new GameNotFoundException("Id: "+id.toString());

        return game;
    }

    /**
     * Persists a game into table T_GAMES
     * @param game to be persisted
     * @return Game
     */
    public Game createGame(Game game) {

        //CompleteDetails
        game.setGameStatus(GameStatus.INITIALIZED);
        //set start score to zero
        game.setScore(0);
        //set creator
        game.setCreatorUsername(game.getCreatorUsername());

        //if gameMode is not specified, set to STANDARD
        if(game.getGameMode()==null){game.setGameMode(GameMode.STANDARD);}

        //if gameName is not specified, set to "Game+ unique integer"
        Date date = new Date();
        if(game.getGameName()==null){game.setGameName("Game"+date.hashCode());}


        gameRepository.save(game);
        gameRepository.flush();

        log.debug("Created Information for Game: {}", game);
        return game;
    }


    public void removeGame(Game game) {
        gameRepository.delete(game);
    }
    /**
     * starts a game if it exists
     * @param gameId of game to be started
     * @return Game
     */
    public Game startGame(Long gameId){
        //get game by id
        Game game = getGame(gameId);
        game.setGameStatus(GameStatus.RUNNING);
        //set Role of players if there are more than minPlayers player
        Set<RealPlayer> players = game.getPlayers();
        int minPlayers = 2;
        if(players.size() < minPlayers){
            throw new NotEnoughPlayersException(String.valueOf(minPlayers));
        }
        //set all players to ROLE.CLUE_WRITER
        for (RealPlayer player: players){
            player.setRole(Role.CLUE_WRITER);
        }
        //set one player to ROLE.GUESSER
        players.iterator().next().setRole(Role.GUESSER);
        //store changes
        gameRepository.save(game);
        gameRepository.flush();

        return game;
    }

    public Game addPlayer(Long id, RealPlayer player) {

        //find game by id
        Game game = getGame(id);

        //exception thrown if game doesn't exist
        if(game == null) {
            throw new GameNotFoundException("Id: " + id.toString());
        }

        //exception if game already has five players
        if(game.getPlayers().size() >= 5) {
            throw new GameFullException(" : Game already has five players.");
        }

        //exception if player is already in the game
        if(game.getPlayers().contains(player)) {
            throw new PlayerAlreadyInGameException("Id: " + player.getPlayerId().toString());
        }

        //create new set and add player if game has no players yet
        if(game.getPlayers() == null) {
            Set<RealPlayer> players = new HashSet<>();
            players.add(player);
            game.setPlayers(players);
        }

        //get players already in the game and add new player
        else {
            Set<RealPlayer> players = game.getPlayers();
            players.add(player);
            game.setPlayers(players);
        }

        log.debug("Added player: {} to game: {}", player, game);
        return game;
    }


    public Game removePlayer(Long gameId, RealPlayer player) {

        //find game by id
        Game game = getGame(gameId);

        //exception thrown if game doesn't exist
        if(game == null) {
            throw new GameNotFoundException("Id: " + gameId.toString());
        }

        //exception if player is not in the game
        if(!game.getPlayers().contains(player)) {
            throw new PlayerNotInGameException("Id: " + player.getPlayerId().toString());
        }

        //get players already in the game and remove player
        Set<RealPlayer> players = game.getPlayers();
        players.remove(player);
        game.setPlayers(players);

        log.debug("Removed player: {} to game: {}", player, game);
        return game;
    }

}
