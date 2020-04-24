package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Clue.ClueGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Clue.CluePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Vote.VotePutDTO;
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

    @PostMapping("/games/{gameId}/players/{playerId}/clue/{clueId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ClueGetDTO submitClue(@PathVariable Long gameId, @PathVariable Long playerId, @PathVariable Long clueId, @RequestBody CluePostDTO cluePostDTO) {

        //get word, owner, and clue
        String word = DTOMapper.INSTANCE.convertCluePostDTOtoClueEntity(cluePostDTO).getWord();
        RealPlayer owner = playerService.getPlayerByPlayerId(playerId);
        Clue clue = clueService.getClue(clueId);

        //add word and owner to clue and set to valid
        clue = clueService.submitClue(clue, owner, word);

        //get running round
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRunningRound(game);

        //validate all clues
        //clueService.validateClues(round);

        //set endTime of clue
        clueService.setEndTime(clue);

        return DTOMapper.INSTANCE.convertClueEntityToClueGetDTO(clue);
    }

    @GetMapping("/games/{gameId}/rounds/{roundNum}/clues")
    @ResponseStatus(HttpStatus.OK)
    public List<ClueGetDTO> getClues(@PathVariable Long gameId, @PathVariable int roundNum){

        //get list of clues for round
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRoundByRoundNum(game, roundNum);

        //validate all clues
        //clueService.validateClues(round);

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
    public ClueGetDTO validateClue(@PathVariable Long gameId, @PathVariable int roundNum, @PathVariable Long clueId, @RequestBody VotePutDTO votePutDTO) {
        Vote vote = DTOMapper.INSTANCE.convertVotePutDTOtoVoteEntity(votePutDTO);

        Clue clue = clueService.getClueById(clueId);
        clue = clueService.manuallyValidateClues(clue,vote);

        return DTOMapper.INSTANCE.convertClueEntityToClueGetDTO(clue);
    }
}
