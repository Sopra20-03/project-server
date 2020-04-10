package ch.uzh.ifi.seal.soprafs20.exceptions.Game;



public class NotEnoughPlayersException extends RuntimeException {
    public NotEnoughPlayersException(String message) {super(message + " : is the minimum Number of Player for a game. Add more Player to the game"); }
}

