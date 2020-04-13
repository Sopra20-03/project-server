package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Guess.GuessPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Round.RoundGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.RoundService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoundController {

    private final RoundService roundService;
    private final GameService gameService;
    private final PlayerService playerService;

    public RoundController(RoundService roundService, GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.roundService = roundService;
        this.playerService = playerService;
    }

    @GetMapping("/games/{id}/rounds")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<RoundGetDTO> getRoundsOfGame(@PathVariable Long id) {
        Game game = gameService.getGame(id);
        //get all rounds of this game
        List<Round> rounds = roundService.getRoundsOfGame(game);
        //convert all rounds to RoundGetDTO
        List<RoundGetDTO> roundGetDTOs= new ArrayList<>();
        for (Round round: rounds){
            roundGetDTOs.add(DTOMapper.INSTANCE.convertRoundEntityToRoundGetDTO(round));
        }
        return roundGetDTOs;
    }
    /**
    @PostMapping("/games/{gameId}/rounds/{roundId}/guess")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public List<RoundGetDTO> submitGuess(@PathVariable Long gameId, @PathVariable Long roundId, @RequestBody GuessPostDTO guessPostDTO){
        //create a new guess
        Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoGuessEntity(guessPostDTO);
        //add submitter of the guess to the guess
        RealPlayer player = playerService.getPlayer(guessPostDTO.getUserId());
        roundService.setGuess(roundId,guess);

    }
    */
}
