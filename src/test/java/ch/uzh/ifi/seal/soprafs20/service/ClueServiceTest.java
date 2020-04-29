package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.ClueWithIdAlreadySubmitted;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.PlayerAlreadySubmittedClueException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.PlayerIsNotClueWriterException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ClueServiceTest {

    @Autowired
    private GameService gameService;
    @Autowired
    private RoundService roundService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private WordCardService wordCardService;

    public PlayerIsNotClueWriterException playerIsNotClueWriterException;
    public List<WordCard> cards;
    public Game testGame;
    public RealPlayer testPlayer;
    public Clue clue;
    public Round activeRound;

    @BeforeEach
    void setup(){
        cards = wordCardService.getWordCards(12);
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame, cards);
        testGame = roundService.startFirstRound(testGame);
        //create player
        testPlayer = new RealPlayer();
        testPlayer.setUserName("test");
        testPlayer.setUserId(1L);
        testPlayer.setRole(Role.CLUE_WRITER);
        testPlayer = playerService.createPlayer(testPlayer, testGame);
        //add clue
        clue = new Clue();
        //get active round
        activeRound = roundService.getRunningRound(testGame);
    }

    @AfterEach
    void tearDown() {
        cards.clear();
        testGame = null;
        testPlayer = null;
        clue = null;
        activeRound = null;
    }

    @Test
    void setClueAsClueWriter() {
        clue.setWord("testClue");
        clue = clueService.setClue(activeRound, testPlayer, clue);
        //check if guess is stored in repo and accessible from the game

        assertEquals("testClue",clueService.getClueById(clue.getClueId()).getWord());


    }

    @Test
    void setClueAsGuesser() {
        testPlayer.setRole(Role.GUESSER);
        clue.setWord("testClue");
        //check if exception is thrown
        PlayerIsNotClueWriterException thrown = assertThrows(
                PlayerIsNotClueWriterException.class, () -> clueService.setClue(activeRound, testPlayer, clue)
        );
    }

    @Test
    void setSeveralCluesAsOnePlayer() {
        //set clue
        clue = clueService.setClue(activeRound, testPlayer, clue);
        //add second clue
        Clue clue2 = new Clue();
        clue2.setWord("testClue");
        //check if exception is thrown
        PlayerAlreadySubmittedClueException thrown = assertThrows(
                PlayerAlreadySubmittedClueException.class, () -> clueService.setClue(activeRound, testPlayer, clue2)
        );
    }

    @Test
    void submitClue() {
        clue = clueService.submitClue(clue, testPlayer, "testClue");
        //assertions
        assertEquals(clue.getIsValid(), true);
        assertEquals(clue.getOwner(), testPlayer);
        assertEquals(clue.getWord(), "testClue");
    }

    @Test
    void submitClueAsGuesser() {
        testPlayer.setRole(Role.GUESSER);

        //check if exception is thrown
        PlayerIsNotClueWriterException thrown = assertThrows(
                PlayerIsNotClueWriterException.class, () -> clueService.submitClue(clue, testPlayer, "testClue")
        );
    }

    //TODO:fix
    @Test
    void submitClueWithClueIdAlreadySubmitted() {
        //create second player
        RealPlayer testPlayer2 = new RealPlayer();
        testPlayer2.setUserName("test2");
        testPlayer2.setUserId(7L);
        testPlayer2.setRole(Role.CLUE_WRITER);
        testPlayer2 = playerService.createPlayer(testPlayer2, testGame);
        RealPlayer finalTestPlayer2 = testPlayer2;

        //submit clue once
        clue = clueService.submitClue(clue, testPlayer, "testClue");

        //check if exception is thrown
        ClueWithIdAlreadySubmitted thrown = assertThrows(
                ClueWithIdAlreadySubmitted.class, () -> clueService.submitClue(clue, finalTestPlayer2, "testClue")
        );
    }

    @Test
    void submitBotClue() {
        clue = clueService.submitBotClue(activeRound, "testClue");
        //assertions
        assertEquals(clue.getIsValid(), true);
        assertEquals(clue.getWord(), "testClue");
        assertEquals(clue.getOwner(), null);
    }

    @Test
    void validateCluesPositiveVote() {
        //select word from WordCard
        wordCardService.selectWord(activeRound, "Alcatraz");
        //set and validate clue
        clue = clueService.setClue(activeRound, testPlayer, clue);
        clueService.validateClues(activeRound);
        //check clue is valid
        assertEquals(true, clue.getIsValid());

    }

    /*
    @Test
    void setEmptyClues() {
        //init testGame
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");

        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame, cards);
        testGame = roundService.startFirstRound(testGame);

        //loads testGame again out of Database
        testGame = gameService.getGame(1L);

        //create User
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setDateCreated(LocalDate.now());
        testUser.setId(1L);

        //create player
        RealPlayer testPlayer = new RealPlayer();
        testPlayer.setUserName("test");
        testPlayer.setUserId(1L);
        testPlayer.setRole(Role.CLUE_WRITER);
        testGame = playerService.addPlayer(testGame, testPlayer, testUser);
        testPlayer = playerService.createPlayer(testPlayer, testGame);
        RealPlayer finalTestPlayer = testPlayer;

        //get running round
        Round activeRound = roundService.getRunningRound(testGame);

        //set empty clues
        testGame = clueService.setEmptyClues(testGame, activeRound);


        //assertions
        assertEquals(1, activeRound.getClues().size());
        assertEquals(testPlayer, activeRound.getClues().get(0).getOwner());
    }
    */

    //TODO: this somehow doesn't work
    /*
    @Test
    void validateCluesNegativeVote() {
        //init testGame
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame, cards);
        testGame = roundService.startFirstRound(testGame);

        //loads testGame again out of Database
        testGame = gameService.getGame(1L);

        //get active round
        Round activeRound = roundService.getRunningRound(testGame);

        //select word from WordCard
        wordCardService.selectWord(activeRound, "Alcatraz");

        //create player
        RealPlayer testPlayer = new RealPlayer();
        testPlayer.setUserName("test");
        testPlayer.setUserId(1L);
        testPlayer.setRole(Role.CLUE_WRITER);
        testPlayer = playerService.createPlayer(testPlayer, testGame);

        //add clue
        Clue clue = new Clue();
        clue.setClueId(1L);
        clue.setRound(activeRound);
        clue = clueService.submitClue(clue, testPlayer, "testClue");
        activeRound.addClue(clue);
        clue.setVotes(-1);

        clueService.validateClues(activeRound);

        //check clue is valid
        assertEquals(false, clue.getIsValid());
    }
    */

    /*
    @Test
    void validateSameClues() {
        //init testGame
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");

        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame, cards);
        testGame = roundService.startFirstRound(testGame);

        //loads testGame again out of Database
        testGame = gameService.getGame(1L);

        //get active round
        Round activeRound = roundService.getRunningRound(testGame);

        //select word from WordCard
        wordCardService.selectWord(activeRound, "Alcatraz");

        //create two players
        RealPlayer testPlayer1 = new RealPlayer();
        testPlayer1.setUserName("test");
        testPlayer1.setUserId(1L);
        testPlayer1.setRole(Role.CLUE_WRITER);
        testPlayer1 = playerService.createPlayer(testPlayer1, testGame);

        RealPlayer testPlayer2 = new RealPlayer();
        testPlayer2.setUserName("test2");
        testPlayer2.setUserId(2L);
        testPlayer2.setRole(Role.CLUE_WRITER);
        testPlayer2 = playerService.createPlayer(testPlayer2, testGame);

        //add clue
        Clue clue = new Clue();
        clue.setWord("testClue");
        clue = clueService.setClue(activeRound, testPlayer1, clue);

        //add same clue
        Clue sameClue = new Clue();
        sameClue.setWord("testClue");
        sameClue = clueService.setClue(activeRound, testPlayer2, sameClue);

        clueService.validateClues(activeRound);

        //check if both clues are Invalid
        List<Clue> clues = clueService.getClues(activeRound);
        for(Clue clueToCheck: clues){
            assertEquals(false, clueToCheck.getIsValid());
        }
    }
     */
}
