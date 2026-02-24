package Labs.lab_05.src.eventbus.exception;

public class MissingSubscriptionException extends Exception {

    public MissingSubscriptionException() {
        super();
    }

    public MissingSubscriptionException(String message) {
        super(message);
    }

    public MissingSubscriptionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
