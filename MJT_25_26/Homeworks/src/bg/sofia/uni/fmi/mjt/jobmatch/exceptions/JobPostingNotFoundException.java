package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.exceptions;

public class JobPostingNotFoundException extends  RuntimeException {
    public JobPostingNotFoundException() {
        super();
    }

    public JobPostingNotFoundException(String message) {
        super(message);
    }

    public JobPostingNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}