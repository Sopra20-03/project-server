package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Clue.ClueGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Clue.CluePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Clue.CluePutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.ClueService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.RoundService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        RealPlayer owner = playerService.getPlayerByPlayerId(playerId);

        //add clue to the game
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRunningRound(game);
        clue = clueService.setClue(round, owner, clue);

        //validate all clues
        clueService.validateClues(round);
        return DTOMapper.INSTANCE.convertClueEntityToClueGetDTO(clue);
    }

    @GetMapping("/games/{gameId}/rounds/{roundNum}/clues")
    @ResponseStatus(HttpStatus.OK)
    public List<ClueGetDTO> getClues(@PathVariable Long gameId, @PathVariable int roundNum){

        //get list of clues for round
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRoundByRoundNum(game, roundNum);
        List<Clue> clues = clueService.getClues(round);

        //convert all clues to ClueGetDTO
        List<ClueGetDTO> clueGetDTOS = new ArrayList<>();
        for (Clue clue : clues) {
            clueGetDTOS.add(DTOMapper.INSTANCE.convertClueEntityToClueGetDTO(clue));
        }
        return clueGetDTOS;
    }

    @PutMapping("/games/{gameId}/rounds/{roundNum}/clues/{clueId}")
    @ResponseStatus(HttpStatus.OK)
    public ClueGetDTO validateClue(@PathVariable Long gameId, @PathVariable int roundNum, @PathVariable Long clueId, @RequestBody CluePutDTO cluePutDTO) {
        Boolean vote = DTOMapper.INSTANCE.convertCluePutDTOtoClueEntity(cluePutDTO).getVotes().get(0);
        Game game = gameService.getGame(gameId);

        Clue clue = clueService.manuallyValidateClues(game, clueId, vote);

        return DTOMapper.INSTANCE.convertClueEntityToClueGetDTO(clue);
    }
}
