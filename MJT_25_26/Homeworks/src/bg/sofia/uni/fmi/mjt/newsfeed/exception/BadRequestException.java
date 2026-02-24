package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception;

public class BadRequestException extends NewsApiException {
    public BadRequestException(String message) {
        super("Bad request " + message);
    }
}
