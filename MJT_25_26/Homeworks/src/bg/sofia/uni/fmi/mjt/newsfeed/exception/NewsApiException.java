package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception;

public class NewsApiException extends Exception {
    public NewsApiException(String message) {
        super(message);
    }

    public NewsApiException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
