package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

public class PlayerIsNotClueWriterException extends RuntimeException {
    public PlayerIsNotClueWriterException(String message) { super(message + " : player does not have the role of Clue Writer."); }
}
