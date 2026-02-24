package bg.sofia.uni.fmi.mjt.fittrack.exception;

public class OptimalPlanImpossibleException extends Exception {

    public OptimalPlanImpossibleException() {
        super();
    }

    public OptimalPlanImpossibleException(String message) {
        super(message);
    }

    public OptimalPlanImpossibleException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
