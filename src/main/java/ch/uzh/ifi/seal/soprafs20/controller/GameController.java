package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Player.PlayerPutDTO;
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
    private final PlayerService playerService;
    private final UserService userService;
    private final WordCardService wordCardService;

    public GameController(GameService gameService, RoundService roundService, PlayerService playerService, UserService userService, WordCardService wordCardService) {
        this.gameService = gameService;
        this.roundService = roundService;
        this.playerService = playerService;
        this.userService = userService;
        this.wordCardService = wordCardService;
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

        // Create game
        game = gameService.createGame(game);

        //create rounds
        game = roundService.createRounds(game);

        //add WordCard to each round
        game = wordCardService.addWordCardsToRounds(game);

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

    @PutMapping("games/{id}/players")
    @ResponseStatus(HttpStatus.OK)
    public GameGetDTO addPlayer(@PathVariable Long id, @RequestBody PlayerPutDTO playerPutDTO) {

        //convert JSON
        RealPlayer player = DTOMapper.INSTANCE.convertPlayerPutDTOtoPlayerEntity(playerPutDTO);

        //get Game to add player to
        Game game = gameService.getGame(id);

        //create player
        player = playerService.createPlayer(player, game);

        game = gameService.addPlayer(id, player);

        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);

        return gameGetDTO;
    }

    /*
    @PutMapping("/games/{id}/rounds/{id}/actions")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GameGetDTO updateRound(@PathVariable Long gameId, @PathVariable Long roundId) {

    }
    */
}
