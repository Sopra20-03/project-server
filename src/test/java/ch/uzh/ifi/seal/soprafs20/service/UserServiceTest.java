package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    private GameService gameService;
    @Autowired
    private RoundService roundService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private UserService userService;

    @Autowired
    private WordCardService wordCardService;

    public List<WordCard> cards;
    public Game testGame;
    public RealPlayer testPlayer1;
    public RealPlayer testPlayer2;
    public User testUser1;
    public User testUser2;


    public Round activeRound;

    @BeforeEach
    void setup(){
        //create test User1
        testUser1 = new User();
        testUser1.setName("testName");
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword");
        testUser1.setToken("testToken");
        testUser1.setStatus(UserStatus.OFFLINE);
        testUser1.setDateCreated(LocalDate.now());
        testUser1.setId(1L);
        testUser1 = userService.createUser(testUser1);
        //create test User
        testUser2 = new User();
        testUser2.setName("testName");
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword");
        testUser2.setToken("testToken");
        testUser2.setStatus(UserStatus.OFFLINE);
        testUser2.setDateCreated(LocalDate.now());
        testUser2.setId(2L);
        testUser2 = userService.createUser(testUser2);

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
        testGame.setScore(11);
        testGame = gameService.finishGame(testGame);
    }


    @Test
    void updateUserScore() {
        testUser1 = userService.updateUserScore(testUser1);
        assertEquals(1, testUser1.getNrOfPlayedGames());
        assertEquals(11, testUser1.getTotalGameScore());
        assertEquals(0, testUser1.getTotalIndividualScore());

    }

    @Test
    void getUsersTest() {
        //check if both created users get returned
        assertEquals(2, userService.getUsers().size());
    }

    @Test
    void getUserTest() {
        User user = userService.getUser(1L);
        //check if correct user gets returned with .getUser()
        assertEquals(testUser1.getId(), user.getId());
    }

    @Test
    void updateUserTest() {
        User updatedUser = userService.getUser(1L);
        updatedUser.setUsername("newUsername");
        //change userName
        userService.updateUser(1L, updatedUser);
        assertEquals("newUsername", userService.getUser(1L).getUsername());
    }

    @Test
    void loginUserTest() {
        //check if user Offline before login
        assertEquals(UserStatus.OFFLINE, testUser1.getStatus());
        //check if user Online after login
        testUser1 = userService.loginUser("testUsername1");
        assertEquals(UserStatus.ONLINE, testUser1.getStatus());
    }

    @Test
    void logoutUserTest() {
        //check if user Offline before login
        assertEquals(UserStatus.OFFLINE, testUser1.getStatus());
        //check if user Online after login
        testUser1 = userService.loginUser("testUsername1");
        assertEquals(UserStatus.ONLINE, testUser1.getStatus());
        //check if user Offline again after logout
        testUser1 = userService.logoutUser("testUsername1");
        assertEquals(UserStatus.OFFLINE, testUser1.getStatus());
    }
}