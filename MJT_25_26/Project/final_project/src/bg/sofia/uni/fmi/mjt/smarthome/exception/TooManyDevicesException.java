package bg.sofia.uni.fmi.mjt.smarthome.exception;

public class TooManyDevicesException extends RuntimeException {
    public TooManyDevicesException() {
        super();
    }

    public TooManyDevicesException(String message) {
        super(message);
    }

    public TooManyDevicesException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
