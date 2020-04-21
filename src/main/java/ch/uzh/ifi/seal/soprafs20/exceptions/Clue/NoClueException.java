package ch.uzh.ifi.seal.soprafs20.exceptions.Clue;

public class NoClueException extends RuntimeException{
    public NoClueException(String message){super("No Clues in Round with RoundId: "+message);}
}
