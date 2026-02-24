package bg.sofia.uni.fmi.mjt.smarthome.exception;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException() {
        super();
    }

    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
