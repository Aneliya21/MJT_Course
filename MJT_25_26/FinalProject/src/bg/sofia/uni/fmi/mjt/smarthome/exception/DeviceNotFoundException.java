package bg.sofia.uni.fmi.mjt.smarthome.exception;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException() {
        super();
    }

    public DeviceNotFoundException(String message) {
        super(message);
    }

    public DeviceNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
