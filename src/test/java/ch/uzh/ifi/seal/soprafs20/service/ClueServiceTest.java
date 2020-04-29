package ch.uzh.ifi.seal.soprafs20.service;

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
    public RealPlayer testPlayer1;
    public RealPlayer testPlayer2;
    public RealPlayer testPlayer3;
    public RealPlayer testPlayer5;
    public RealPlayer testPlayer6;
    public RealPlayer testPlayer8;
    public Clue clue;
    public Clue clue1;
    public Clue clue2;
    public Clue clue3;
    public Clue clue4;
    public Clue clue5;
    public Clue clue6;
    public Clue clue7;
    public Clue clue8;
    public Round activeRound;

    @BeforeEach
    void setup(){
        cards = wordCardService.getWordCards(13);
        //init testGame
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        testGame = gameService.createGame(testGame);
        activeRound = new Round();
        activeRound.setGame(testGame);
        activeRound.setRoundNum(1);
        activeRound.setRoundStatus(RoundStatus.RUNNING);
        activeRound.setWordCard(cards.get(0));
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

        assertEquals("testClue1",clueService.getClueById(clue1.getClueId()).getWord());
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
        clue2 = new Clue();
        clue2.setWord("testClue");


        //check if exception is thrown
        PlayerIsNotClueWriterException thrown = assertThrows(
                PlayerIsNotClueWriterException.class, () -> clueService.setClue(activeRound, testPlayer2, clue2)
        );
    }

    @Test
    void setSeveralCluesAsOnePlayer() {
        //createPlayer
        testPlayer3 = new RealPlayer();
        testPlayer3.setUserName("testUser3");
        testPlayer3.setUserId(3L);
        testPlayer3.setRole(Role.CLUE_WRITER);
        testPlayer3 = playerService.createPlayer(testPlayer3, testGame);
        //set clue
        clue3 = new Clue();
        clue3.setWord("testClue");
        clue = clueService.setClue(activeRound, testPlayer3, clue3);
        //add second clue
        clue4 = new Clue();
        clue4.setWord("testClue");
        //check if exception is thrown
        PlayerAlreadySubmittedClueException thrown = assertThrows(
                PlayerAlreadySubmittedClueException.class, () -> clueService.setClue(activeRound, testPlayer3, clue4)
        );
    }

    @Test
    void submitClue() {
        //create player
        testPlayer5 = new RealPlayer();
        testPlayer5.setUserName("testUser5");
        testPlayer5.setUserId(5L);
        testPlayer5.setRole(Role.CLUE_WRITER);
        testPlayer5 = playerService.createPlayer(testPlayer5, testGame);
        //set clue
        clue5 = new Clue();
        clue5 = clueService.submitClue(clue5, testPlayer5, "testClue");
        //assertions
        assertEquals(clue5.getIsValid(), true);
        assertEquals(clue5.getOwner(), testPlayer5);
        assertEquals(clue5.getWord(), "testClue");
    }

    @Test
    void submitClueAsGuesser() {
        //create player
        testPlayer6 = new RealPlayer();
        testPlayer6.setUserName("testUser6");
        testPlayer6.setUserId(6L);
        testPlayer6.setRole(Role.GUESSER);
        testPlayer6 = playerService.createPlayer(testPlayer6, testGame);
        //set clue
        clue6 = new Clue();

        //check if exception is thrown
        PlayerIsNotClueWriterException thrown = assertThrows(
                PlayerIsNotClueWriterException.class, () -> clueService.submitClue(clue6, testPlayer6, "testClue")
        );
    }

    @Test
    void submitBotClue() {
        clue7 = new Clue();
        clue7 = clueService.submitBotClue(activeRound, "testClue");
        //assertions
        assertEquals(clue7.getIsValid(), true);
        assertEquals(clue7.getWord(), "testClue");
        assertEquals(clue7.getOwner(), null);
    }

    @Test
    void validateCluesPositiveVote() {
        //create player
        testPlayer8 = new RealPlayer();
        testPlayer8.setUserName("testUser8");
        testPlayer8.setUserId(8L);
        testPlayer8.setRole(Role.CLUE_WRITER);
        testPlayer8 = playerService.createPlayer(testPlayer8, testGame);
        //select word from WordCard
        wordCardService.selectWord(activeRound, "Alcatraz");
        //set and validate clue
        clue8 = new Clue();
        clue8 = clueService.setClue(activeRound, testPlayer8, clue8);
        clueService.validateClues(activeRound);
        //check clue is valid
        assertEquals(true, clue8.getIsValid());

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
