package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.RoundService;
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

    public GameController(GameService gameService, RoundService roundService) {
        this.gameService = gameService;
        this.roundService = roundService;
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
}
