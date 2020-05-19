package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Game Controller
 * This class is responsible for handling all REST request that are related to the game.
 * The controller will receive the request and delegate the execution to the GameService and finally return the result.
 */
@RestController
public class GameController {

    private final GameService gameService;
    private final RoundService roundService;
    private final WordCardService wordCardService;

    private final PlayerService playerService;

    public GameController(GameService gameService, RoundService roundService, WordCardService wordCardService, PlayerService playerService) {
        this.gameService = gameService;
        this.roundService = roundService;
        this.wordCardService = wordCardService;
        this.playerService = playerService;

    }

    @GetMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String lobby() {
        return "SoPra Group 03 Server Application is Running. Path: /";
    }

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameGetDTO> getAllGames() {
        // Fetch all games as List of POJOs
        List<Game> games = gameService.getGames();
        List<GameGetDTO> gameGetDTOS = new ArrayList<>();

        // Convert each game POJO to the JSON
        for (Game game : games) {
            gameGetDTOS.add(DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game));
        }
        return gameGetDTOS;
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@RequestBody GamePostDTO gamePostDTO) {
        // Convert JSON  to POJO
        Game game = DTOMapper.INSTANCE.convertGamePostDTOtoGameEntity(gamePostDTO);

        List<WordCard> cards = wordCardService.getWordCards(13);

        // Create game
        game = gameService.createGame(game);

        //create rounds
        game = roundService.createRounds(game, cards);

        // Convert POJO to JSON
        return DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
    }

    @DeleteMapping("/games/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO deleteGame(@PathVariable Long id) {
        // Get game details
        Game game = gameService.getGame(id);

        //remove all players from game
        game= playerService.removeAllPlayer(game);

        // Delete rounds from game
        roundService.removeRounds(game);

        // Delete game
        gameService.removeGame(game);

        // Convert POJO to JSON
        return DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
    }

    @GetMapping("/games/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable Long id) {
        Game game = gameService.getGame(id);
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);

        return gameGetDTO;
    }

    @PutMapping("games/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameGetDTO startGame(@PathVariable Long id){
        //start game
        Game game = gameService.startGame(id);
        //start first round
        game = roundService.startFirstRound(game);
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
        return gameGetDTO;
    }

    /**
     * Method for testing:
     * changes round status
     * @param id
     * @return
     */
    @PutMapping("games/{id}/skip")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GameGetDTO skipRound(@PathVariable Long id){
        Game game = gameService.getGame(id);
        // when all rounds are finished, finish the game
        if(roundService.lastRoundFinished(game)){
            game = gameService.finishGame(game);
        }
        //else start the next round
        else {
            game = roundService.startNextRound(game);
        }
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
        return gameGetDTO;
    }


}
