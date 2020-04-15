package ch.uzh.ifi.seal.soprafs20.exceptions.Round;

public class RoundNotFoundException extends RuntimeException {
    public RoundNotFoundException() {
        super(" : Round is not in Game.");
    }
}