package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.WordCard;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.List;
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
class GuessServiceTest {

    @Autowired
    private GameService gameService;
    @Autowired
    private RoundService roundService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private GuessService guessService;
    @Autowired
    private WordCardService wordCardService;

    public Game testGame;
    public List<WordCard> cards;




    @BeforeEach
    void setup(){

        cards = wordCardService.getWordCards(12);
    }
/*
    @Test
    void setGuess() {

        //create 2 Users
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setDateCreated(LocalDate.now());
        testUser.setId(1L);
        User testUser2 = new User();
        testUser2.setName("testName2");
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword");
        testUser2.setToken("testToken2");
        testUser2.setStatus(UserStatus.OFFLINE);
        testUser2.setDateCreated(LocalDate.now());
        testUser2.setId(2L);

        //create Game
        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setGameName("testGame");
        List<WordCard> cards = wordCardService.getWordCards(13);
        testGame = gameService.createGame(testGame);
        testGame = roundService.createRounds(testGame, cards);

        //create player and add to game
        RealPlayer testPlayer = new RealPlayer();
        testPlayer.setUserId(1L);
        RealPlayer testPlayer2 = new RealPlayer();
        testPlayer2.setUserId(2L);
        testGame = playerService.addPlayer(testGame, testPlayer, testUser);
        testPlayer = playerService.createPlayer(testPlayer, testGame);
        testGame = playerService.addPlayer(testGame, testPlayer2, testUser2);
        testPlayer2 = playerService.createPlayer(testPlayer2, testGame);

        //start Game
        testGame = gameService.startGame(testGame.getGameId());
        testGame = roundService.startFirstRound(testGame);

        //get current round and select word
        Round round = roundService.getRunningRound(testGame);
        round.setRoundStatus(RoundStatus.RUNNING);
        //wordCardService.selectWord(round, "testWord");

        //select word
        cards.get(0).setSelectedWord("testWord");
        round.setWordCard(cards.get(0));

        //add guess
        Guess guess = new Guess();
        guess.setWord("testGuess");
        guess.setOwner(testPlayer);

        guess = guessService.setGuess(round,guess);
        //check if guess is stored in repo and accessible from the game
        assertEquals("testGuess",guessService.getGuess(round).getWord());

    }
    @Test
    void validateGuess(){
        Guess guess = new Guess();
        guess.setWord("testWORD");

        WordCard wordCard = new WordCard();
        wordCard.setSelectedWord("testWord");
        assertTrue(guessService.correctGuess(wordCard, guess));
    }
    */

}