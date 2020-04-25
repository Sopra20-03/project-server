package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

public class ClueNotInRoundException extends RuntimeException{
    public ClueNotInRoundException(String clueId, String roundId) { super("clue: " + clueId + " not in round: " + roundId); }
}
