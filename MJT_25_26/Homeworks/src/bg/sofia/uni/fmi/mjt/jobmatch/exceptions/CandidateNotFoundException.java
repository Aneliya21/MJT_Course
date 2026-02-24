package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.exceptions;

public class CandidateNotFoundException extends RuntimeException {
    public CandidateNotFoundException() {
        super();
    }

    public CandidateNotFoundException(String message) {
        super(message);
    }

    public CandidateNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
