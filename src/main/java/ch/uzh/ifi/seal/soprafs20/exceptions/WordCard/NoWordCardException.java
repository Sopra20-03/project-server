package ch.uzh.ifi.seal.soprafs20.exceptions.WordCard;

public class NoWordCardException extends RuntimeException {
    public NoWordCardException(String message) {super(message + " : has no WordCard assigned yet."); }
}