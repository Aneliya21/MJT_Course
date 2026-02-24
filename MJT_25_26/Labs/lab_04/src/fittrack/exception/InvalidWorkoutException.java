package bg.sofia.uni.fmi.mjt.fittrack.exception;

public class InvalidWorkoutException extends RuntimeException {

    public InvalidWorkoutException() {
        super();
    }

    public InvalidWorkoutException(String message) {
        super(message);
    }

    public InvalidWorkoutException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
