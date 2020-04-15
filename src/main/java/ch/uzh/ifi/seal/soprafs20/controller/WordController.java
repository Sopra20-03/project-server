package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
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

@RestController
public class WordController {
    private final RoundService roundService;
    private final GameService gameService;
    private final WordCardService wordCardService;

    public WordController(RoundService roundService, GameService gameService, WordCardService wordCardService) {
        this.gameService = gameService;
        this.roundService = roundService;
        this.wordCardService = wordCardService;

    }
    @PutMapping("/games/{gameId}/rounds/{roundId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoundGetDTO selectWord(@PathVariable Long gameId, @PathVariable Long roundId, @RequestBody WordCardPutDTO wordCardPutDTO) {
        //Convert JSON
        WordCard wordCard = DTOMapper.INSTANCE.convertWordCardPutDTOtoWordCardEntity(wordCardPutDTO);

        //get Game
        Game game = gameService.getGame(gameId);

        //get Round
        Round round = roundService.getRoundById(game, roundId);

        //set selectedWord
        round = wordCardService.setSelectedWord(round, wordCard.getSelectedWord());

        //Convert to JSON
        RoundGetDTO roundGetDTO = DTOMapper.INSTANCE.convertRoundEntityToRoundGetDTO(round);

        return roundGetDTO;
    }
}