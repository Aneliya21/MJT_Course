package Homeworks.src.bg.sofia.uni.fmi.mjt.space.exception;

public class CipherException extends Exception {

    public CipherException() {
        super();
    }

    public CipherException(String message) {
        super(message);
    }

    public CipherException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

