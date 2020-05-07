package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;
    private final PlayerService playerService;

    UserController(UserService userService, PlayerService playerService) {
        this.userService = userService;
        this.playerService = playerService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // Fetch all users as List of POJOs
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // Convert each user POJO to JSON
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertUserEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // Convert user JSON to POJO
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoUserEntity(userPostDTO);

        // Create user
        User createdUser = userService.createUser(userInput);

        // Convert POJO to JSON
        return DTOMapper.INSTANCE.convertUserEntityToUserGetDTO(createdUser);
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertUserEntityToUserGetDTO(user);
        return userGetDTO;
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public UserGetDTO updateUser(@PathVariable Long id ,@RequestBody UserPutDTO userPutDTO) {

        // Convert user JSON to POJO
        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        //Update User
        user = userService.updateUser(id,user);

        // Convert POJO to JSON
        return DTOMapper.INSTANCE.convertUserEntityToUserGetDTO(user);
    }


}
