package ch.uzh.ifi.seal.soprafs20.exceptions.Round;

public class NoRunningRoundException extends RuntimeException {
    public NoRunningRoundException(String message){super("Game :"+ message+"has no running round");}
}
