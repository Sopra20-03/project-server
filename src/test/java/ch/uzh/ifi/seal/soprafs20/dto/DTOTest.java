package ch.uzh.ifi.seal.soprafs20.dto;

import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Clue.ClueGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Clue.CluePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Clue.CluePutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Guess.GuessGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Guess.GuessPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Message.MessageGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Message.MessagePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Player.PlayerGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Player.PlayerPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Round.RoundGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Vote.VotePutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.WordCard.WordCardPutDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Test all getters and setters of DTO Mapper
 */
@SpringBootTest
public class DTOTest {
    @Test
    public void ClueGetDTOTest(){
        ClueGetDTO testClueGetDTO = new ClueGetDTO();
        testClueGetDTO.setClueId(1L);
        assertEquals(1L,testClueGetDTO.getClueId());
        testClueGetDTO.setIsValid(true);
        assertTrue(testClueGetDTO.getIsValid());
        testClueGetDTO.setOwnerId(1L);
        assertEquals(1L,testClueGetDTO.getOwnerId());
        testClueGetDTO.setWord("testWord");
        assertEquals("testWord",testClueGetDTO.getWord());
        testClueGetDTO.setVotes(1);
        assertEquals(1,testClueGetDTO.getVotes());
        testClueGetDTO.setVoteCount(12);
        assertEquals(12,testClueGetDTO.getVoteCount());
        testClueGetDTO.setTotalTime(10);
        assertEquals(10, testClueGetDTO.getTotalTime());

    }
    @Test
    public void CluePostDTO(){
        CluePostDTO testCluePostDTO = new CluePostDTO();
        testCluePostDTO.setWord("testWord");
        assertEquals("testWord",testCluePostDTO.getWord());
    }
    @Test
    public void CluePutDTO(){
        CluePutDTO testCluePutDTO = new CluePutDTO();
        testCluePutDTO.setVotes(true);
        assertTrue(testCluePutDTO.getVotes());
    }
    @Test
    public void GameGetDTOTest(){
        GameGetDTO testGameGetDTO = new GameGetDTO();
        testGameGetDTO.setGameId(1L);
        assertEquals(1L, testGameGetDTO.getGameId());
        
        testGameGetDTO.setGameName("testName");
        assertEquals("testName", testGameGetDTO.getGameName());

        LocalDate date = LocalDate.now();
        testGameGetDTO.setDateCreated(date);
        assertEquals(date, testGameGetDTO.getDateCreated());
        
        testGameGetDTO.setCreatorUsername("user");
        assertEquals("user", testGameGetDTO.getCreatorUsername());
        
        testGameGetDTO.setPlayerCount(0);
        assertEquals(0,testGameGetDTO.getPlayerCount());
        
        testGameGetDTO.setGameStatus(GameStatus.FINISHED);
        assertEquals(GameStatus.FINISHED, testGameGetDTO.getGameStatus());
        
        testGameGetDTO.setGameMode(GameMode.STANDARD);
        assertEquals(GameMode.STANDARD, testGameGetDTO.getGameMode());
        
        testGameGetDTO.setBotMode(BotMode.FRIENDLY);
        assertEquals(BotMode.FRIENDLY, testGameGetDTO.getBotMode());
        
        testGameGetDTO.setScore(0);
        assertEquals(0,testGameGetDTO.getScore());

        testGameGetDTO.setDuration(Duration.SHORT);
        assertEquals(Duration.SHORT,testGameGetDTO.getDuration());
        
    }
    
    @Test
    public void GamePostDTOTest(){
        GamePostDTO testGamePostDTO = new GamePostDTO();
        testGamePostDTO.setGameName("gameName");
        assertEquals("gameName", testGamePostDTO.getGameName());

        testGamePostDTO.setCreatorUsername("creator");
        assertEquals("creator", testGamePostDTO.getCreatorUsername());

        testGamePostDTO.setGameMode(GameMode.STANDARD);
        assertEquals(GameMode.STANDARD, testGamePostDTO.getGameMode());

        testGamePostDTO.setBotMode(BotMode.FRIENDLY);
        assertEquals(BotMode.FRIENDLY, testGamePostDTO.getBotMode());

        testGamePostDTO.setDuration(Duration.SHORT);
        assertEquals(Duration.SHORT,testGamePostDTO.getDuration());
    }

    @Test
    public void GuessGetDTOTest(){
        GuessGetDTO testGuessGetDTO = new GuessGetDTO();
        testGuessGetDTO.setGuessId(1L);
        assertEquals(1L,testGuessGetDTO.getGuessId());

        testGuessGetDTO.setWord("testWord");
        assertEquals("testWord", testGuessGetDTO.getWord());

        testGuessGetDTO.setIsValid(true);
        assertTrue(testGuessGetDTO.getIsValid());
    }
    @Test
    void GuessPostDTOTest(){
        GuessPostDTO testGuessPostDTO = new GuessPostDTO();
        testGuessPostDTO.setWord("testWord");
        assertEquals("testWord", testGuessPostDTO.getWord());
    }

    @Test
    void MessageGetDTOTest(){
        MessageGetDTO testMessageGetDTO = new MessageGetDTO();
        testMessageGetDTO.setMessageId(1L);
        assertEquals(1L, testMessageGetDTO.getMessageId());

        testMessageGetDTO.setUsername("testUser");
        assertEquals("testUser", testMessageGetDTO.getUsername());

        testMessageGetDTO.setText("text");
        assertEquals("text", testMessageGetDTO.getText());

        LocalDateTime date = LocalDateTime.now();
        testMessageGetDTO.setTimeCreated(date);
        assertEquals(date, testMessageGetDTO.getTimeCreated());

    }
    @Test
    void MessagePostDTOTest(){
        MessagePostDTO testMessagePostDTO = new MessagePostDTO();
        testMessagePostDTO.setUsername("testUser");
        assertEquals("testUser",testMessagePostDTO.getUsername());

        testMessagePostDTO.setText("text");
        assertEquals("text", testMessagePostDTO.getText());
    }

    @Test
    void PlayerGetDTOTest(){
        PlayerGetDTO testPlayerGetDTO = new PlayerGetDTO();
        testPlayerGetDTO.setPlayerId(1L);
        assertEquals(1L, testPlayerGetDTO.getPlayerId());

        testPlayerGetDTO.setUserId(1L);
        assertEquals(1L,testPlayerGetDTO.getUserId());

        testPlayerGetDTO.setUserName("user");
        assertEquals("user",testPlayerGetDTO.getUserName());

        testPlayerGetDTO.setRole(Role.CLUE_WRITER);
        assertEquals(Role.CLUE_WRITER, testPlayerGetDTO.getRole());

        testPlayerGetDTO.setScore(0);
        assertEquals(0,testPlayerGetDTO.getScore());
    }

    @Test
    void PlayerPutDTOTest(){
        PlayerPutDTO testPlayerPutDTO = new PlayerPutDTO();
        testPlayerPutDTO.setUserId(1L);
        assertEquals(1L,testPlayerPutDTO.getUserId());
    }
    @Test
    void RoundGetDTOTest(){
        RoundGetDTO testRoundGetDTO = new RoundGetDTO();
        testRoundGetDTO.setRoundId(1L);
        assertEquals(1L,testRoundGetDTO.getRoundId());

        testRoundGetDTO.setRoundNum(1);
        assertEquals(1,testRoundGetDTO.getRoundNum());

        testRoundGetDTO.setRoundStatus(RoundStatus.RUNNING);
        assertEquals(RoundStatus.RUNNING, testRoundGetDTO.getRoundStatus());

        WordCard wordCard = new WordCard();
        testRoundGetDTO.setWordCard(wordCard);
        assertEquals(wordCard,testRoundGetDTO.getWordCard());
    }
    @Test
    void UserGetDTOTest(){
        UserGetDTO testUserGetDTO = new UserGetDTO();
        testUserGetDTO.setId(1L);
        assertEquals(1L, testUserGetDTO.getId());

        testUserGetDTO.setName("name");
        assertEquals("name", testUserGetDTO.getName());

        testUserGetDTO.setUsername("userName");
        assertEquals("userName", testUserGetDTO.getUsername());

        testUserGetDTO.setStatus(UserStatus.ONLINE);
        assertEquals(UserStatus.ONLINE, testUserGetDTO.getStatus());

        LocalDate date = LocalDate.now();
        testUserGetDTO.setDateCreated(date);
        assertEquals(date,testUserGetDTO.getDateCreated());

        testUserGetDTO.setToken("token");
        assertEquals("token", testUserGetDTO.getToken());

        testUserGetDTO.setIcon("icon");
        assertEquals("icon", testUserGetDTO.getIcon());

        testUserGetDTO.setNrOfPlayedGames(1);
        assertEquals(1, testUserGetDTO.getNrOfPlayedGames());

        testUserGetDTO.setTotalGameScore(1);
        assertEquals(1,testUserGetDTO.getTotalGameScore());

        testUserGetDTO.setTotalIndividualScore(1);
        assertEquals(1,testUserGetDTO.getTotalIndividualScore());
    }

    @Test
    void UserPostDTOTest(){
        UserPostDTO testUserPostDTO = new UserPostDTO();
        testUserPostDTO.setName("name");
        assertEquals("name", testUserPostDTO.getName());

        testUserPostDTO.setPassword("123");
        assertEquals("123", testUserPostDTO.getPassword());

        testUserPostDTO.setUsername("userName");
        assertEquals("userName", testUserPostDTO.getUsername());
    }
    @Test
    void UserPutDTOTest(){
        UserPutDTO testUserPutDTO = new UserPutDTO();
        testUserPutDTO.setName("name");
        assertEquals("name", testUserPutDTO.getName());

        testUserPutDTO.setUsername("userName");
        assertEquals("userName", testUserPutDTO.getUsername());

        testUserPutDTO.setIcon("icon");
        assertEquals("icon", testUserPutDTO.getIcon());
    }

    @Test
    void VotePutDTOTest(){
        VotePutDTO testVotePutDTO = new VotePutDTO();
        testVotePutDTO.setVote(true);
        assertTrue(testVotePutDTO.getVote());
    }
    @Test
    void WordCardPutDTO(){
        WordCardPutDTO testWordCardPutDTO = new WordCardPutDTO();
        testWordCardPutDTO.setSelectedWord("selectedWord");
        assertEquals("selectedWord", testWordCardPutDTO.getSelectedWord());
    }
}
