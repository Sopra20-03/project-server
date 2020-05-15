package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Player.PlayerGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Player.PlayerPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlayerController {

    private final PlayerService playerService;
    private final GameService gameService;
    private final UserService userService;


    public PlayerController(PlayerService playerService, GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.userService = userService;

    }

    @GetMapping("/games/{id}/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlayerGetDTO> getPlayers(@PathVariable Long id) {
        Game game = gameService.getGame(id);

        //get all players of this game
        List<RealPlayer> players = playerService.getPlayersByGame(game);

        //convert all players to PlayerGetDTO
        List<PlayerGetDTO> playerGetDTOs = new ArrayList<>();
        for (RealPlayer player : players) {
            playerGetDTOs.add(DTOMapper.INSTANCE.convertPlayerEntityToPlayerGetDTO(player));
        }
        return playerGetDTOs;
    }
    @PutMapping("games/{id}/players")
    @ResponseStatus(HttpStatus.OK)
    public GameGetDTO addPlayer(@PathVariable Long id, @RequestBody PlayerPutDTO playerPutDTO) {

        //gets userId as playerPutDTO
        RealPlayer player = DTOMapper.INSTANCE.convertPlayerPutDTOtoPlayerEntity(playerPutDTO);

        //get Game to add player to
        Game game = gameService.getGame(id);

        //search User just to check if it exists
        User user = userService.getUser(player.getUserId());
        // create and add player
        game = playerService.addPlayer(game, player, user);

        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);

        return gameGetDTO;
    }

    @DeleteMapping("games/{gameId}/players/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removePlayer(@PathVariable Long gameId, @PathVariable Long userId) {
        Game game = gameService.getGame(gameId);
        playerService.removePlayer(game, userId);
    }

    //I put the setIndividualScore inside submitClue
    /*
    @PutMapping("games/{gameId}/rounds/{roundNum}/score")
    @ResponseStatus(HttpStatus.OK)
    public List<PlayerGetDTO> setIndividualScore(@PathVariable Long gameId, @PathVariable int roundNum) {
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRoundByRoundNum(game, roundNum);
        List<Clue> clues = clueService.getClues(round);
        List<RealPlayer> players = playerService.getPlayersByGame(game);

        for(Clue clue : clues) {
            int score = clueService.calculateIndividualScore(round, clue);
            playerService.setScore(clue.getOwnerId(), score);
        }

        List<PlayerGetDTO> playerGetDTOs = new ArrayList<>();
        for (RealPlayer player : players) {
            playerGetDTOs.add(DTOMapper.INSTANCE.convertPlayerEntityToPlayerGetDTO(player));
        }
        return playerGetDTOs;
    }
    */
}
