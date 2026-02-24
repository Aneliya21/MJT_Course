package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception;

public class ServerErrorException extends NewsApiException {
    public ServerErrorException(String message) {
        super("Server error " + message);
    }
}
