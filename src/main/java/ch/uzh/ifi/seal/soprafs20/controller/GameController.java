package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
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
    private final UserService userService;
    private final PlayerService playerService;

    public GameController(GameService gameService, UserService userService, PlayerService playerService) {
        this.gameService = gameService;
        this.userService = userService;
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

        // Create game
        Game newGame = gameService.createGame(game);

        // Convert POJO to JSON
        return DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(newGame);
    }

    @GetMapping("/games/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getUser(@PathVariable Long id) {
        Game game = gameService.getGame(id);
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
        return gameGetDTO;
    }

    @PutMapping("games/{id}/player")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GameGetDTO addPlayer(@PathVariable Long id, String token) {
        User user = new User();
        user = userService.getUserByToken(token);

        RealPlayer player = playerService.createPlayer(user);

        Game game = new Game();
        game = gameService.addPlayer(id, player);

        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
        return gameGetDTO;
    }
}
