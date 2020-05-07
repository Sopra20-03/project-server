package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UserAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test controller class UserController
 * This allows us to test the REST API calls and validate the request & response
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }


    @Test
    public void test() throws Exception {
        System.out.println("Test");
    }

    /*
     * Assertions
     * assertEquals(Expected, Actual)
     */

    //Test case 1 : GET /users
    /**
     GET /users
     Test: GET /users
     Result: 200 Success & list of users should be given back
     */
    @Test
    @WithMockUser(username = "testUsername")
    public void getUsersSuccess() throws Exception {
        // given
        testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setId(1L);
        testUser.setDateCreated(LocalDate.now());
        testUser.setNrOfPlayedGames(0);
        testUser.setTotalGameScore(0);
        testUser.setTotalIndividualScore(0);

        List<User> allUsers = Collections.singletonList(testUser);
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(testUser.getUsername())))
                .andExpect(jsonPath("$[0].name", is(testUser.getName().toString())))
                .andExpect(jsonPath("$[0].dateCreated", is(testUser.getDateCreated().toString())))
                .andExpect(jsonPath("$[0].status", is(testUser.getStatus().toString())))
                .andExpect(jsonPath("$[0].nrOfPlayedGames", is(testUser.getNrOfPlayedGames())))
                .andExpect(jsonPath("$[0].totalGameScore", is(testUser.getTotalGameScore())))
                .andExpect(jsonPath("$[0].totalIndividualScore", is(testUser.getTotalIndividualScore())))
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(200, result.getResponse().getStatus());
        //Check Correct HTTP Response Content-Type
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        //Check Correct HTTP Request Data Passing
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getRequest().getContentType());
    }

    //Test case 2 : GET /users/{id}
    /**
     GET /users/{id}
     Test: GET /users/{id} with valid user id for which the user exists
     Result: 200 Success with user details
     */
    @Test
    @WithMockUser(username = "testUsername")
    public void getUserSuccess() throws Exception {
        // given
        testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setId(1L);
        testUser.setDateCreated(LocalDate.now());
        testUser.setNrOfPlayedGames(1);
        testUser.setTotalGameScore(2);
        testUser.setTotalIndividualScore(3);

        given(userService.getUser(Mockito.any())).willReturn(testUser);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/1");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.name", is(testUser.getName().toString())))
                .andExpect(jsonPath("$.dateCreated", is(testUser.getDateCreated().toString())))
                .andExpect(jsonPath("$.status", is(testUser.getStatus().toString())))
                .andExpect(jsonPath("$.nrOfPlayedGames", is(testUser.getNrOfPlayedGames())))
                .andExpect(jsonPath("$.totalGameScore", is(testUser.getTotalGameScore())))
                .andExpect(jsonPath("$.totalIndividualScore", is(testUser.getTotalIndividualScore())))
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(200, result.getResponse().getStatus());
        //Check Correct HTTP Response Content-Type (Data Format)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        //Check Correct HTTP Response Data
        assertEquals("{\"id\":1,\"name\":\"testName\",\"username\":\"testUsername\",\"status\":\"OFFLINE\",\"dateCreated\":\""+LocalDate.now()+"\",\"token\":\"testToken\",\"nrOfPlayedGames\":1,\"totalGameScore\":2,\"totalIndividualScore\":3}", result.getResponse().getContentAsString());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());

    }

    /**
     GET /users/{id}
     Test: GET /users/{id} with invalid user id for which the user doesn't exists
     Result: 404 Not Found Error
     */
    @Test
    @WithMockUser(username = "testUsername")
    public void getUserError() throws Exception {
        // given
        testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setId(1L);
        testUser.setDateCreated(LocalDate.now());

        given(userService.getUser(Mockito.any())).willThrow(UserNotFoundException.class);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/2");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(404, result.getResponse().getStatus());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());

    }

    //Test case 3 : POST /users
    /**
     POST /users
     Test: POST /users with valid valid data
     Result: 201 Created and Successfully added the user
     */
    @Test
    @WithMockUser(username = "testUsername")
    public void createUserSuccess() throws Exception {
        // given
        testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setId(1L);
        testUser.setDateCreated(LocalDate.now());
        testUser.setNrOfPlayedGames(1);
        testUser.setTotalGameScore(2);
        testUser.setTotalIndividualScore(3);

        //User from Post
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setName("testName");
        userPostDTO.setPassword("testPassword");

        given(userService.createUser(Mockito.any())).willReturn(testUser);

        // when
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO))
                .characterEncoding("utf-8");

        // then
        MvcResult result = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.name", is(testUser.getName().toString())))
                .andExpect(jsonPath("$.dateCreated", is(testUser.getDateCreated().toString())))
                .andExpect(jsonPath("$.status", is(testUser.getStatus().toString())))
                .andExpect(jsonPath("$.nrOfPlayedGames", is(testUser.getNrOfPlayedGames())))
                .andExpect(jsonPath("$.totalGameScore", is(testUser.getTotalGameScore())))
                .andExpect(jsonPath("$.totalIndividualScore", is(testUser.getTotalIndividualScore())))
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(201, result.getResponse().getStatus());
        //Check Correct HTTP Response Content-Type (Data Format)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        //Check Correct HTTP Response Data
        assertEquals("{\"id\":1,\"name\":\"testName\",\"username\":\"testUsername\",\"status\":\"OFFLINE\",\"dateCreated\":\""+LocalDate.now()+"\",\"token\":\"testToken\",\"nrOfPlayedGames\":1,\"totalGameScore\":2,\"totalIndividualScore\":3}", result.getResponse().getContentAsString());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.POST.name(), result.getRequest().getMethod());
        //Check Correct HTTP Request Data Passing
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getRequest().getContentType());
    }

    /**
     POST /users
     Test: POST /users with details but already used username
     Result: 409 Conflict and Error in adding the user
     */

    @Test
    @WithMockUser(username = "testUsername")
    public void createUserError() throws Exception {
        // given
        testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setId(1L);
        testUser.setDateCreated(LocalDate.now());

        //User from Post
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setName("testName");
        userPostDTO.setPassword("testPassword");

        given(userService.createUser(Mockito.any())).willThrow(UserAlreadyExistsException.class);

        // when
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO))
                .characterEncoding("utf-8");

        // then
        MvcResult result = mockMvc.perform(postRequest)
                .andDo(print())
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(409, result.getResponse().getStatus());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.POST.name(), result.getRequest().getMethod());
        //Check Correct HTTP Request Data Passing
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getRequest().getContentType());

    }

    //Test case 4 : PUT /users/{id}
    /**
     PUT /users/{id}
     Test: PUT /users/{id} with valid details and valid user id for which the user exists
     Result: 204 No Content. Update Successful
     */
    @Test
    @WithMockUser(username = "testUsername")
    public void updateUserSuccess() throws Exception {
        // given
        testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setId(1L);
        testUser.setDateCreated(LocalDate.now());

        given(userService.updateUser(Mockito.any(),Mockito.any())).willReturn(testUser);

        // when
        MockHttpServletRequestBuilder updateRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"oldTestUsername\",\"name\":\"oldTestName\"}")
                .characterEncoding("utf-8");

        // then
        MvcResult result = mockMvc.perform(updateRequest)
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(204, result.getResponse().getStatus());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());

    }

    /**
     PUT /users/{id}
     Test: PUT /users/{id} with valid user id for which the user exists but new username already taken
     Result: 404 Not Found
     */
    @Test
    @WithMockUser(username = "testUsername")
    public void updateUserFail() throws Exception {
        // given
        testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setId(1L);
        testUser.setDateCreated(LocalDate.now());

        given(userService.updateUser(Mockito.any(),Mockito.any())).willThrow(UserNotFoundException.class);

        // when
        MockHttpServletRequestBuilder updateRequest = put("/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"oldTestUsername\",\"name\":\"oldTestName\"}")
                .characterEncoding("utf-8");

        // then
        MvcResult result = mockMvc.perform(updateRequest)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        //Assertions
        //Check Correct HTTP Response Status
        assertEquals(404, result.getResponse().getStatus());
        //Check Correct HTTP Request Method
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());
    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }




}