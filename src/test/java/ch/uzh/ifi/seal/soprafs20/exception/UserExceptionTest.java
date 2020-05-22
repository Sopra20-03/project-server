package ch.uzh.ifi.seal.soprafs20.exception;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.EmptyFieldException;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UserAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs20.service.RoundService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
public class UserExceptionTest {
    User testUser1;
    User testUser2;
    @Autowired
    private RoundService roundService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setup() {
        //create test User1
        testUser1 = new User();
        testUser1.setName("testName");
        testUser1.setUsername("testUsername");
        testUser1.setPassword("testPassword");
        testUser1.setToken("testToken");
        testUser1.setStatus(UserStatus.OFFLINE);
        testUser1.setDateCreated(LocalDate.now());
        testUser1.setId(1L);
        testUser1 = userService.createUser(testUser1);
        //create test User2
        testUser2 = new User();
        testUser2.setName("testName");
        testUser2.setUsername("testUsername");
        testUser2.setPassword("testPassword");
        testUser2.setToken("testToken");
        testUser2.setStatus(UserStatus.OFFLINE);
        testUser2.setDateCreated(LocalDate.now());
        testUser2.setId(2L);
    }

    @Test
    void UserAlreadyExistExceptionTest() {
        //exception when try to create same username twice
        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(testUser2);
        });

        String expectedMessage = "User with UserName: testUsername already exists.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void UserNotFoundExceptionTest() {
        //get user when user not exist result in an error
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUser(3L);
        });

        String expectedMessage = "User with Id: 3 doesn't exist.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void EmptyFieldExceptionTest() {
        //updateUser() with UserId = null throws error
        Exception exception = assertThrows(EmptyFieldException.class, () -> {
            userService.updateUser(null, testUser1);
        });

        String expectedMessage = "Id field should not be empty.";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
}