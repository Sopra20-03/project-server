package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Player.PlayerGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlayerController {

    private final PlayerService playerService;
    private final GameService gameService;

    public PlayerController(PlayerService playerService, GameService gameService) {
        this.gameService = gameService;
        this.playerService = playerService;
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
}
