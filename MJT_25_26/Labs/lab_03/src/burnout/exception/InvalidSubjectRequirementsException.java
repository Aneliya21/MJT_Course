package bg.sofia.uni.fmi.mjt.burnout.exception;

public class InvalidSubjectRequirementsException extends Exception {

    public InvalidSubjectRequirementsException() {
    }

    public InvalidSubjectRequirementsException(String message) {
        super(message);
    }

    public InvalidSubjectRequirementsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
