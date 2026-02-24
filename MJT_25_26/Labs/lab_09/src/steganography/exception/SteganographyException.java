package Labs.lab_09.src.steganography.exception;

public class SteganographyException extends RuntimeException {

    public SteganographyException() {
        super();
    }

    public SteganographyException(String message) {
        super(message);
    }

    public SteganographyException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
