package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.GameNotFoundException;
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
    private final RoundService roundService;



    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, RoundService roundService) {
        this.gameRepository = gameRepository;
        this.roundService = roundService;
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
        game.setTimeCreated(new Date());

        //if gameMode is not specified, set to STANDARD
        if(game.getGameMode()==null){game.setGameMode(GameMode.STANDARD);}

        //if gameName is not specified, set to "Game+ unique integer"
        if(game.getGameName()==null){game.setGameName("Game"+game.getTimeCreated().hashCode());}



        // saves the given entity but data is only persisted in the database once flush() is called
        gameRepository.save(game);
        gameRepository.flush();

        roundService.createRounds(game);

        log.debug("Created Information for Game: {}", game);
        return game;
    }

    public List<Round> getRounds(Long gameId){
        return gameRepository.getOne(gameId).getRounds();
    }

    public Game addPlayer(Long id, RealPlayer player) {

        //TODO: add exception if Id doesn't exist

        //find game by id
        Game game = getGame(id);

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

        //TODO: add exception if game already has five players

        log.debug("Added player: {} to game: {}", player, game);
        return game;
    }

}
