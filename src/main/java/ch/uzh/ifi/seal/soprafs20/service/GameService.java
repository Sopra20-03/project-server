package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Game Service
 * This class is the "worker" and responsible for all functionality related to the game
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class GameService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

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
    public Game createUser(Game game) {

        //CompleteDetails
        game.setGameStatus(GameStatus.INITIALIZED);

        // saves the given entity but data is only persisted in the database once flush() is called
        gameRepository.save(game);
        gameRepository.flush();

        log.debug("Created Information for Game: {}", game);
        return game;
    }
}
