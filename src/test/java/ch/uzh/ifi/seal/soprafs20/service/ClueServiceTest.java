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

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setup(){
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void setClueAsClueWriter() {
        //init WordCard
        List<WordCard> cards1 = wordCardService.getWordCards(12);
        //init testGame
        Game testGame1 = new Game();
        testGame1.setGameId(1L);
        testGame1.setGameName("testGame");
        testGame1 = gameService.createGame(testGame1);
        testGame1 = roundService.createRounds(testGame1, cards1);
        testGame1 = roundService.startFirstRound(testGame1);
        //create player
        RealPlayer testPlayer1 = new RealPlayer();
        testPlayer1.setUserName("test");
        testPlayer1.setUserId(1L);
        testPlayer1.setRole(Role.CLUE_WRITER);
        testPlayer1 = playerService.createPlayer(testPlayer1, testGame1);
        //add clue
        Clue clue1 = new Clue();
        //get active round
        Round activeRound1 = roundService.getRunningRound(testGame1);

        clue1.setWord("testClue");
        clue1 = clueService.setClue(activeRound1, testPlayer1, clue1);
        //check if guess is stored in repo and accessible from the game
        assertEquals("testClue",clueService.getClueById(clue1.getClueId()).getWord());
    }

    @Test
    void setClueAsGuesser() {
        List<WordCard> cards2 = wordCardService.getWordCards(12);
        //init testGame
        Game testGame2 = new Game();
        testGame2.setGameId(2L);
        testGame2.setGameName("testGame");
        testGame2 = gameService.createGame(testGame2);
        testGame2 = roundService.createRounds(testGame2, cards2);
        testGame2 = roundService.startFirstRound(testGame2);
        //create player
        RealPlayer testPlayer2 = new RealPlayer();
        testPlayer2.setUserName("test");
        testPlayer2.setUserId(2L);
        testPlayer2.setRole(Role.GUESSER);
        testPlayer2 = playerService.createPlayer(testPlayer2, testGame2);
        RealPlayer finalTestPlayer2 = testPlayer2;
        //add clue
        Clue clue2 = new Clue();
        //get active round
        Round activeRound2 = roundService.getRunningRound(testGame2);

        clue2.setWord("testClue");
        //check if exception is thrown
        PlayerIsNotClueWriterException thrown = assertThrows(
                PlayerIsNotClueWriterException.class, () -> clueService.setClue(activeRound2, finalTestPlayer2, clue2)
        );
    }

    @Test
    void setSeveralCluesAsOnePlayer() {
        List<WordCard> cards3 = wordCardService.getWordCards(12);
        //init testGame
        Game testGame3 = new Game();
        testGame3.setGameId(3L);
        testGame3.setGameName("testGame");
        testGame3 = gameService.createGame(testGame3);
        testGame3 = roundService.createRounds(testGame3, cards3);
        testGame3 = roundService.startFirstRound(testGame3);
        //create player
        RealPlayer testPlayer3 = new RealPlayer();
        testPlayer3.setUserName("test");
        testPlayer3.setUserId(3L);
        testPlayer3.setRole(Role.CLUE_WRITER);
        testPlayer3 = playerService.createPlayer(testPlayer3, testGame3);
        RealPlayer finalTestPlayer3 = testPlayer3;
        //add clue
        Clue clue3 = new Clue();
        //get active round
        Round activeRound3 = roundService.getRunningRound(testGame3);

        //set clue
        clue3 = clueService.setClue(activeRound3, testPlayer3, clue3);
        //add second clue
        Clue clue4 = new Clue();
        clue4.setWord("testClue");
        //check if exception is thrown
        PlayerAlreadySubmittedClueException thrown = assertThrows(
                PlayerAlreadySubmittedClueException.class, () -> clueService.setClue(activeRound3, finalTestPlayer3, clue4)
        );
    }

    @Test
    void submitClue() {
        List<WordCard> cards5 = wordCardService.getWordCards(12);
        //init testGame
        Game testGame5 = new Game();
        testGame5.setGameId(5L);
        testGame5.setGameName("testGame");
        testGame5 = gameService.createGame(testGame5);
         testGame5 = roundService.createRounds(testGame5, cards5);
        testGame5 = roundService.startFirstRound(testGame5);
        //create player
        RealPlayer testPlayer5 = new RealPlayer();
        testPlayer5.setUserName("test");
        testPlayer5.setUserId(5L);
        testPlayer5.setRole(Role.CLUE_WRITER);
        testPlayer5 = playerService.createPlayer(testPlayer5, testGame5);
        //add clue
        Clue clue5 = new Clue();
        //get active round
        Round activeRound5 = roundService.getRunningRound(testGame5);

        clue5 = clueService.submitClue(clue5, testPlayer5, "testClue");
        //assertions
        assertTrue(clue5.getIsValid());
        assertEquals(clue5.getOwner(), testPlayer5);
        assertEquals(clue5.getWord(), "testClue");
    }

    @Test
    void submitClueAsGuesser() {
        List<WordCard> cards6 = wordCardService.getWordCards(12);
        //init testGame
        Game testGame6 = new Game();
        testGame6.setGameId(6L);
        testGame6.setGameName("testGame");
        testGame6 = gameService.createGame(testGame6);
        testGame6 = roundService.createRounds(testGame6, cards6);
        testGame6 = roundService.startFirstRound(testGame6);
        //create player
        RealPlayer testPlayer6 = new RealPlayer();
        testPlayer6.setUserName("test");
        testPlayer6.setUserId(6L);
        testPlayer6.setRole(Role.CLUE_WRITER);
        testPlayer6 = playerService.createPlayer(testPlayer6, testGame6);
        RealPlayer finalTestPlayer6 = testPlayer6;
        //add clue
        Clue clue6 = new Clue();
        //get active round
        Round activeRound6 = roundService.getRunningRound(testGame6);

        testPlayer6.setRole(Role.GUESSER);

        //check if exception is thrown
        PlayerIsNotClueWriterException thrown = assertThrows(
                PlayerIsNotClueWriterException.class, () -> clueService.submitClue(clue6, finalTestPlayer6, "testClue")
        );
    }

    @Test
    void submitClueWithClueIdAlreadySubmitted() {
        List<WordCard> cards7 = wordCardService.getWordCards(12);
        //init testGame
        Game testGame7 = new Game();
        testGame7.setGameId(7L);
        testGame7.setGameName("testGame");
        testGame7 = gameService.createGame(testGame7);
        testGame7 = roundService.createRounds(testGame7, cards7);
        testGame7 = roundService.startFirstRound(testGame7);
        //create player
        RealPlayer testPlayer7 = new RealPlayer();
        testPlayer7.setUserName("test");
        testPlayer7.setUserId(7L);
        testPlayer7.setRole(Role.CLUE_WRITER);
        testPlayer7 = playerService.createPlayer(testPlayer7, testGame7);
        //add clue
        Clue clue7 = new Clue();
        //get active round
        Round activeRound7 = roundService.getRunningRound(testGame7);

        //create second player
        RealPlayer testPlayer8 = new RealPlayer();
        testPlayer8.setUserName("test2");
        testPlayer8.setUserId(8L);
        testPlayer8.setRole(Role.CLUE_WRITER);
        testPlayer8 = playerService.createPlayer(testPlayer8, testGame7);
        RealPlayer finalTestPlayer8 = testPlayer8;

        //submit clue once
        clue7.setClueId(7L);
        clue7 = clueService.submitClue(clue7, testPlayer7, "testClue");
        Clue finalClue7 = clue7;

        //check if exception is thrown
        ClueWithIdAlreadySubmitted thrown = assertThrows(
                ClueWithIdAlreadySubmitted.class, () -> clueService.submitClue(finalClue7, finalTestPlayer8, "testClue")
        );
    }

    @Test
    void submitBotClue() {
        List<WordCard> cards9 = wordCardService.getWordCards(12);
        //init testGame
        Game testGame9 = new Game();
        testGame9.setGameId(9L);
        testGame9.setGameName("testGame");
        testGame9 = gameService.createGame(testGame9);
        testGame9 = roundService.createRounds(testGame9, cards9);
        testGame9 = roundService.startFirstRound(testGame9);
        //create player
        RealPlayer testPlayer9 = new RealPlayer();
        testPlayer9.setUserName("test");
        testPlayer9.setUserId(9L);
        testPlayer9.setRole(Role.CLUE_WRITER);
        testPlayer9 = playerService.createPlayer(testPlayer9, testGame9);
        //add clue
        Clue clue9 = new Clue();
        //get active round
        Round activeRound9 = roundService.getRunningRound(testGame9);

        clue9 = clueService.submitBotClue(activeRound9, "testClue");

        //assertions
        assertTrue(clue9.getIsValid());
        assertEquals(clue9.getWord(), "testClue");
        assertNull(clue9.getOwner());
    }

    @Test
    void validateCluesPositiveVote() {
        List<WordCard> cards10 = wordCardService.getWordCards(12);
        //init testGame
        Game testGame10 = new Game();
        testGame10.setGameId(10L);
        testGame10.setGameName("testGame");
        testGame10 = gameService.createGame(testGame10);
        testGame10 = roundService.createRounds(testGame10, cards10);
        testGame10 = roundService.startFirstRound(testGame10);
        //create player
        RealPlayer testPlayer10 = new RealPlayer();
        testPlayer10.setUserName("test");
        testPlayer10.setUserId(10L);
        testPlayer10.setRole(Role.CLUE_WRITER);
        testPlayer10 = playerService.createPlayer(testPlayer10, testGame10);
        //add clue
        Clue clue10 = new Clue();
        //get active round
        Round activeRound10 = roundService.getRunningRound(testGame10);

        //select word from WordCard
        wordCardService.selectWord(activeRound10, "Alcatraz");
        //set and validate clue
        clue10 = clueService.setClue(activeRound10, testPlayer10, clue10);
        clueService.validateClues(activeRound10);
        //check clue is valid
        assertTrue(clue10.getIsValid());
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
