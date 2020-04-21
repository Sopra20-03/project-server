package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Clue.ClueGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Clue.CluePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.ClueService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.RoundService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClueController {
    private final ClueService clueService;
    private final PlayerService playerService;
    private final GameService gameService;
    private final RoundService roundService;

    public ClueController(ClueService clueService, PlayerService playerService, GameService gameService, RoundService roundService) {
        this.clueService = clueService;
        this.playerService = playerService;
        this.gameService = gameService;
        this.roundService = roundService;
    }

    @PostMapping("/games/{gameId}/players/{playerId}/clue")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ClueGetDTO submitClue(@PathVariable Long gameId, @PathVariable Long playerId, @RequestBody CluePostDTO cluePostDTO) {

        //create a new clue
        Clue clue = DTOMapper.INSTANCE.convertCluePostDTOtoClueEntity(cluePostDTO);

        //add owner of the clue
        RealPlayer owner = playerService.getPlayer(playerId);

        //add clue to the game
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRunningRound(game);
        clue = clueService.setClue(round, owner, clue);

        return DTOMapper.INSTANCE.convertClueEntityToClueGetDTO(clue);
    }
}
