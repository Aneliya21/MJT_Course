package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}