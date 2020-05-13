package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.BotMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.Role;
import ch.uzh.ifi.seal.soprafs20.constant.RoundStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.PlayerAlreadySubmittedClueException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Clue.PlayerIsNotClueWriterException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
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
    public RealPlayer testPlayer1;
    public RealPlayer testPlayer2;


    public Clue clue1;
    public Clue clue2;

    public List<Round> rounds;


    public Round activeRound;

    @BeforeEach
    void setup() {
        cards = wordCardService.getWordCards(13);
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame.setCreatorUsername("testUser");
        testGame.setGameMode(GameMode.STANDARD);
        testGame.setBotMode(BotMode.FRIENDLY);
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame, cards);
        List<Round> rounds = roundService.getRoundsOfGame(testGame);
        activeRound = rounds.get(0);
        activeRound.setRoundStatus(RoundStatus.RUNNING);
        //create player1
        testPlayer1 = new RealPlayer();
        testPlayer1.setUserName("testUser1");
        testPlayer1.setUserId(1L);
        testPlayer1.setRole(Role.CLUE_WRITER);
        testPlayer1 = playerService.createPlayer(testPlayer1, testGame);
        //createPlayer 2
        testPlayer2 = new RealPlayer();
        testPlayer2.setUserName("testUser2");
        testPlayer2.setUserId(2L);
        testPlayer2.setRole(Role.CLUE_WRITER);
        testPlayer2 = playerService.createPlayer(testPlayer2, testGame);
        //add active round to game
        rounds = testGame.getRounds();
        rounds.add(activeRound);
        testGame.setRounds(rounds);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void setClueAsClueWriter() {
        //create player
        testPlayer1 = new RealPlayer();
        testPlayer1.setUserName("testUser1");
        testPlayer1.setUserId(1L);
        testPlayer1.setRole(Role.CLUE_WRITER);
        testPlayer1 = playerService.createPlayer(testPlayer1, testGame);
        //create clue
        clue1 = new Clue();
        clue1.setWord("testClue1");
        clue1 = clueService.setClue(activeRound, testPlayer1, clue1);

        assertEquals("testClue1", clueService.getClueById(clue1.getClueId()).getWord());
    }

    @Test
    void setClueAsGuesser() {
        //createPlayer
        testPlayer2 = new RealPlayer();
        testPlayer2.setUserName("testUser2");
        testPlayer2.setUserId(2L);
        testPlayer2.setRole(Role.GUESSER);
        testPlayer2 = playerService.createPlayer(testPlayer2, testGame);
        //create clue
        clue1 = new Clue();
        clue1.setWord("testClue");


        //check if exception is thrown
        PlayerIsNotClueWriterException thrown = assertThrows(
                PlayerIsNotClueWriterException.class, () -> clueService.setClue(activeRound, testPlayer2, clue2)
        );
    }

    @Test
    void setSeveralCluesAsOnePlayer() {

        //set clue
        clue1 = new Clue();
        clue1.setWord("testClue");
        clue1 = clueService.setClue(activeRound, testPlayer1, clue1);
        //add second clue
        clue2 = new Clue();
        clue2.setWord("testClue");
        //check if exception is thrown
        PlayerAlreadySubmittedClueException thrown = assertThrows(
                PlayerAlreadySubmittedClueException.class, () -> clueService.setClue(activeRound, testPlayer1, clue2)
        );
    }

    @Test
    void submitClue() {
        //set clue
        clue1 = new Clue();
        clue1 = clueService.submitClue(clue1, testPlayer1, "testClue");
        //assertions
        assertEquals(clue1.getIsValid(), true);
        assertEquals(clue1.getOwner(), testPlayer1);
        assertEquals(clue1.getWord(), "testClue");
    }

    @Test
    void submitBotClue() {

        clue1 = clueService.submitBotClue(activeRound, "testClue");
        //assertions
        assertEquals(clue1.getIsValid(), true);
        assertEquals(clue1.getWord(), "testClue");
        assertEquals(clue1.getOwner(), null);
    }

    @Test
    void validateCluesPositiveVote() {
        //select word from WordCard
        wordCardService.selectWord(activeRound, "Alcatraz");
        //set and validate clue
        clue1 = new Clue();
        clue1 = clueService.setClue(activeRound, testPlayer1, clue1);
        clueService.validateClues(activeRound);
        //check clue is valid
        assertEquals(true, clue1.getIsValid());

    }

    /**
     * checks if two different clues are valid by auto validation
     */
    @Test
    void autoValidateDifferentClues() {
        //select word from WordCard
        wordCardService.selectWord(activeRound, "Alcatraz");
        //create two clues that are different
        clue1 = new Clue();
        clue1.setWord("Island");
        activeRound.addClue(clue1);
        clue1 = clueService.setClue(activeRound, testPlayer1, clue1);

        clue2 = new Clue();
        clue2.setWord("Prison");
        activeRound.addClue(clue2);
        clue2 = clueService.setClue(activeRound, testPlayer2, clue2);

        //auto validate
        clueService.autoValidateClues(activeRound);
        //check that both clues are valid
        assertTrue(clue1.getIsValid());
        assertTrue(clue2.getIsValid());
    }
    /**
     * checks if two similar clues are invalid by auto validation
     */
    @Test
    void autoValidateSameClues() {

        //select word from WordCard
        wordCardService.selectWord(activeRound, "Alcatraz");
        //create two clues that are the same
        clue1 = new Clue();
        clue1.setWord("Prison");
        activeRound.addClue(clue1);
        clue1 = clueService.setClue(activeRound, testPlayer1, clue1);

        clue2 = new Clue();
        clue2.setWord("prison");
        activeRound.addClue(clue2);
        clue2 = clueService.setClue(activeRound, testPlayer2, clue2);

        //auto validate
        clueService.autoValidateClues(activeRound);
        //check that both clues are invalid
        assertFalse(clue1.getIsValid());
        assertFalse(clue2.getIsValid());
    }
    /**
     * checks if two substrings are invalid by auto validation
     */
    @Test
    void autoValidateSubstring() {

        //select word from WordCard
        wordCardService.selectWord(activeRound, "Alcatraz");
        //create two clues that are the same
        clue1 = new Clue();
        clue1.setWord("San Francisco");
        activeRound.addClue(clue1);
        clue1 = clueService.setClue(activeRound, testPlayer1, clue1);

        clue2 = new Clue();
        clue2.setWord("Francisco");
        activeRound.addClue(clue2);
        clue2 = clueService.setClue(activeRound, testPlayer2, clue2);

        //auto validate
        clueService.autoValidateClues(activeRound);
        //check that both clues are invalid
        assertFalse(clue1.getIsValid());
        assertFalse(clue2.getIsValid());
    }


    /**
     * integration test: (i think?)
     * checks whole process of submitting a clue to calculating the individual score
     */
    @Test
    void scoreCalculationWithTimer() throws InterruptedException {
        //set two clues
        Clue clue1 = new Clue();
        clue1 = clueService.setClue(activeRound, testPlayer1, clue1);
        Clue clue2 = new Clue();
        clue2 = clueService.setClue(activeRound, testPlayer2, clue2);

        //set start and end time for clues and calculate the individual score
        clueService.setStartTime(activeRound);
        clueService.setEndTime(clue1);
        //make system wait some time
        TimeUnit.SECONDS.sleep(10);
        clueService.setEndTime(clue2);
        int score1 = clueService.calculateIndividualScore(activeRound, clue1);
        int score2 = clueService.calculateIndividualScore(activeRound, clue2);
        //assert that clue2 has a lower score than clue1
        assertTrue(score1 > score2);

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
        testPlayer.setUserId(1L)
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


    //TODO: this somehow doesn't work

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
