package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.EmptyFieldException;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UserAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UsernameTakenException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    //Password Encoding
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns a list of all users from table T_USERS
     * @return List<User>
     */
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    /**
     * Returns user with given id from table "T_USERS"
     * @param id of the user to be returned
     * @return User
     */
    public User getUser(Long id){
        User user = userRepository.findUserById(id);

        if(user == null)
            throw new UserNotFoundException("Id: "+id.toString());

        return user;
    }

    //don't need this anymore I think...
    /**
     * Returns a user with given token from table "T_USERS"
     * @param token of the user to be returned
     * @return User
     */
    public User getUserByToken(String token) {
        User user = userRepository.findUserByToken(token);

        if(user == null)
            throw new UserNotFoundException("token: "+token);

        return user;
    }

    /**
     * Persists a user into table T_USERS
     * @param user to be persisted
     * @return User
     */
    public User createUser(User user) {

        //CompleteDetails
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setToken(UUID.randomUUID().toString());
        user.setStatus(UserStatus.OFFLINE);
        user.setDateCreated(LocalDate.now());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setNrOfPlayedGames(0);
        user.setTotalGameScore(0);
        user.setTotalIndividualScore(0);

        //Check if username is available
        checkIfUserAlreadyExists(user);

        // saves the given entity but data is only persisted in the database once flush() is called
        userRepository.save(user);
        userRepository.flush();

        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Updates user details for a user in table T_USERS
     * @param id of the user to be updated
     * @param user instance with updated details
     * @return User
     */
    public User updateUser(Long id, User user) {

        if(id == null)
            throw new EmptyFieldException("Id");

        //Find User by Id
        User findUserById = getUser(id);

        //Only update the necessary details
        findUserById.setName(user.getName() == null ? findUserById.getName() : user.getName());
        //Update Username
        if(!user.getUsername().equals(findUserById.getUsername())){
            checkIfUsernameTaken(user.getUsername());
            findUserById.setUsername(user.getUsername() == null ? findUserById.getUsername() : user.getUsername());
        }

        userRepository.save(findUserById);
        userRepository.flush();

        return findUserById;
    }

    public User loginUser(String username) {
        User user = userRepository.findUserByUsername(username);

        user.setStatus(UserStatus.ONLINE);
        userRepository.save(user);
        userRepository.flush();

        return user;
    }

    public User logoutUser(String username) {
        User user = userRepository.findUserByUsername(username);

        user.setStatus(UserStatus.OFFLINE);
        userRepository.save(user);
        userRepository.flush();

        return user;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username
     * in the User entity to be persisted.
     * The method will do nothing if the input is unique and throw an error otherwise.
     * @param user
     * @throws UserAlreadyExistsException
     * @see User
     */
    private void checkIfUserAlreadyExists(User user) {

        if(userRepository.findUserByUsername(user.getUsername())!=null){
            //User with given username already exists.
            throw new UserAlreadyExistsException(user.getUsername());
        }
        //User doesn't exist
    }

    /**
     * This is a helper method that will check if the username is available or not.
     * The method will do nothing if the username is not already taken and throw an error otherwise.
     * @param username
     * @throws UsernameTakenException
     * @see User
     */
    private void checkIfUsernameTaken(String username){
        if(userRepository.findUserByUsername(username)!=null)
            throw new UsernameTakenException(username);
    }

    public User updateUserScore(long userId, int nrOfPlayedGames, int totalGameScore, int totalIndividualScore){
        User user = getUser(userId);
        user.setNrOfPlayedGames(nrOfPlayedGames);
        user.setTotalGameScore(totalGameScore);
        user.setTotalIndividualScore(totalIndividualScore);
        userRepository.save(user);
        userRepository.flush();
        return user;
    }
}
