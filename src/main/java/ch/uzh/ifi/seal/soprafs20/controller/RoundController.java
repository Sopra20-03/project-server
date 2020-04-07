package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Round.RoundGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.RoundService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoundController {
    private final GameService gameService;
    private final RoundService roundService;

    public RoundController(GameService gameService, RoundService roundService) {
        this.gameService = gameService;
        this.roundService = roundService;
    }

    @GetMapping("/games/{id}/rounds")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<RoundGetDTO> getRoundsOfGame(@PathVariable Long id) {
        //get all rounds of this game
        List<Round> rounds = roundService.getRoundsOfGame(id);
        //convert all rounds to RoundGetDTO
        List<RoundGetDTO> roundGetDTOs= new ArrayList<>();
        for (Round round: rounds){
            roundGetDTOs.add(DTOMapper.INSTANCE.convertRoundEntityToRoundGetDTO(round));
        }
        return roundGetDTOs;
    }
}
