package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Guess.GuessGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Guess.GuessPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Round.RoundGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GuessController {
    private final RoundService roundService;
    private final GameService gameService;
    private final PlayerService playerService;
    private final GuessService guessService;

    public GuessController(RoundService roundService, GameService gameService, PlayerService playerService, GuessService guessService){
        this.roundService = roundService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.guessService = guessService;

    }

     @PostMapping("/games/{gameId}/players/{playerId}/guess")
     @ResponseStatus(HttpStatus.CREATED)
     @ResponseBody
     public GuessGetDTO submitGuess(@PathVariable Long gameId, @PathVariable Long playerId, @RequestBody GuessPostDTO guessPostDTO){
     //create a new guess
     Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoGuessEntity(guessPostDTO);
     //add submitter of the guess to the guess
     RealPlayer player = playerService.getPlayer(playerId);
     guess.setOwner(player);
     //store guess in the game
     Game game = gameService.getGame(gameId);
     Round round = roundService.getRunningRound(game);
     guess = guessService.setGuess(round,guess);
     return DTOMapper.INSTANCE.convertGuessEntityToGuessGetDTO(guess);
     }
    @GetMapping("/games/{gameId}/rounds/{roundId}/guess")
    @ResponseStatus(HttpStatus.OK)
    public GuessGetDTO getGuess(@PathVariable Long gameId, @PathVariable Long roundId){
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRoundById(game, roundId);
        Guess guess = guessService.getGuess(round);
        return DTOMapper.INSTANCE.convertGuessEntityToGuessGetDTO(guess);
     }
}
