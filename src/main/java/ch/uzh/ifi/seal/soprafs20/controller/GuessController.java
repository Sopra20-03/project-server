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

import java.util.List;

@RestController
public class GuessController {
    private final RoundService roundService;
    private final GameService gameService;
    private final PlayerService playerService;

    public GuessController(RoundService roundService, GameService gameService, PlayerService playerService){
        this.roundService = roundService;
        this.gameService = gameService;
        this.playerService = playerService;

    }

     @PostMapping("/games/{gameId}/players/{playerId}/guess")
     @ResponseStatus(HttpStatus.CREATED)
     @ResponseBody
     public RoundGetDTO submitGuess(@PathVariable Long gameId, @PathVariable Long playerId, @RequestBody GuessPostDTO guessPostDTO){
     //create a new guess
     Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoGuessEntity(guessPostDTO);
     //add submitter of the guess to the guess
     RealPlayer player = playerService.getPlayer(playerId);
     guess.setOwner(player);
     //store guess in the game
     Game game = gameService.getGame(gameId);
     Round round = roundService.setGuess(game,guess);
     return DTOMapper.INSTANCE.convertRoundEntityToRoundGetDTO(round);
     }

}
