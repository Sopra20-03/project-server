package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Round;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Round.RoundGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.WordCard.WordCardPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.ClueService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.RoundService;
import ch.uzh.ifi.seal.soprafs20.service.WordCardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class WordController {
    private final RoundService roundService;
    private final GameService gameService;
    private final WordCardService wordCardService;
    private final ClueService clueService;

    public WordController(RoundService roundService, GameService gameService, WordCardService wordCardService, ClueService clueService) {
        this.gameService = gameService;
        this.roundService = roundService;
        this.wordCardService = wordCardService;
        this.clueService = clueService;
    }
    @PutMapping("/games/{gameId}/rounds/{roundNum}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoundGetDTO selectWord(@PathVariable Long gameId, @PathVariable int roundNum, @RequestBody WordCardPutDTO wordCardPutDTO) {
        //Convert JSON
        WordCard wordCard = DTOMapper.INSTANCE.convertWordCardPutDTOtoWordCardEntity(wordCardPutDTO);

        //get Game
        Game game = gameService.getGame(gameId);

        //get Round
        Round round = roundService.getRoundByRoundNum(game, roundNum);

        //set selectedWord
        round = wordCardService.setSelectedWord(round, wordCard.getSelectedWord());

        //set startTime in each clue of round
        clueService.setStartTime(round);

        //Convert to JSON
        RoundGetDTO roundGetDTO = DTOMapper.INSTANCE.convertRoundEntityToRoundGetDTO(round);

        return roundGetDTO;
    }
}
