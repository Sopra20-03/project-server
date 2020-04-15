package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.entity.RealPlayer;
import ch.uzh.ifi.seal.soprafs20.entity.Round;

import ch.uzh.ifi.seal.soprafs20.rest.dto.Guess.GuessPostDTO;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;

import ch.uzh.ifi.seal.soprafs20.rest.dto.Round.RoundGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.WordCard.WordCardPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.RoundService;
import ch.uzh.ifi.seal.soprafs20.service.WordCardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoundController {

    private final RoundService roundService;
    private final GameService gameService;


    public RoundController(RoundService roundService, GameService gameService) {
        this.gameService = gameService;
        this.roundService = roundService;

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


    @GetMapping("/games/{gameId}/rounds/{roundId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoundGetDTO getRoundById(@PathVariable Long gameId, @PathVariable Long roundId) {

        //get Game
        Game game = gameService.getGame(gameId);

        //get Round
        Round round = roundService.getRoundById(game, roundId);

        //convert Round to RoundGetDTO
        RoundGetDTO roundGetDTO = DTOMapper.INSTANCE.convertRoundEntityToRoundGetDTO(round);

        return roundGetDTO;
    }



}
