package ch.uzh.ifi.seal.soprafs20.exceptions;

import ch.uzh.ifi.seal.soprafs20.exceptions.Game.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.NotEnoughPlayersException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.PlayerAlreadyInGameException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Game.PlayerNotInGameException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Guess.NoGuessException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Guess.RoundHasAlreadyGuessException;
import ch.uzh.ifi.seal.soprafs20.exceptions.Round.RoundNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UserAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.User.UsernameTakenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    /**
     * UserNotFound Exception
     * Throws HTTP 404 NOT FOUND
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleUserNotFoundException(UserNotFoundException ex) {
        log.error(String.format("UserNotFoundException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * UsernameTaken Exception
     * Throws HTTP 409 CONFLICT
     */
    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity handleUsernameTakenException(UsernameTakenException ex) {
        log.error(String.format("UsernameTakenException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * UserAlreadyExists Exception
     * Throws HTTP 409 CONFLICT
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error(String.format("UserAlreadyExistsException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * EmptyField Exception
     * Throws HTTP 400 BAD REQUEST
     */
    @ExceptionHandler(EmptyFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleEmptyFieldException(EmptyFieldException ex) {
        log.error(String.format("EmptyFieldException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * GameNotFound Exception
     * Throws HTTP 404 NOT FOUND
     */
    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleGameNotFoundException(GameNotFoundException ex) {
        log.error(String.format("GameNotFoundException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseStatusException handleTransactionSystemException(Exception ex, HttpServletRequest request) {
        log.error("Request: {} raised {}", request.getRequestURL(), ex);
        return new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
    }

    // Keep this one disable for all testing purposes -> it shows more detail with this one disabled
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseStatusException handleException(Exception ex) {
        log.error("Default Exception Handler -> caught:", ex);
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
    }

    /**
     * No Guess found Exception
     * Throws HTTP 404 NOT FOUND
     */
    @ExceptionHandler(NoGuessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleNoGuessException(NoGuessException ex) {
        log.error(String.format("NoGuessException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    /**
     * Round has already Guess Exception
     * Throws HTTP 409 CONFLICT
     */
    @ExceptionHandler(RoundHasAlreadyGuessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity handleRoundHasAlreadyGuessException(RoundHasAlreadyGuessException ex) {
        log.error(String.format("RoundHasAlreadyGuessException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }
    /**
     * No Guess found Exception
     * Throws HTTP 404 NOT FOUND
     */
    @ExceptionHandler(PlayerNotInGameException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handlePlayerNotInGameException(PlayerNotInGameException ex) {
        log.error(String.format("PlayerNotInGameException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    /**
     * PlayerAlreadyInGame Exception
     * Throws HTTP 400 BAD REQUEST
     */
    @ExceptionHandler(PlayerAlreadyInGameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handlePlayerAlreadyInGameException(PlayerAlreadyInGameException ex) {
        log.error(String.format("PlayerAlreadyInGameException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    /**
     * No Round found Exception
     * Throws HTTP 404 NOT FOUND
     */
    @ExceptionHandler(RoundNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleRoundNotFoundException(RoundNotFoundException ex) {
        log.error(String.format("RoundNotFoundException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    /**
     * NotEnoughPlayer Exception
     * Throws HTTP 400 BAD REQUEST
     */
    @ExceptionHandler(NotEnoughPlayersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleNotEnoughPlayersException(PlayerAlreadyInGameException ex) {
        log.error(String.format("NotEnoughPlayersException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}