package Labs.lab_10.src.music.server.repository.exception;

public class PlaylistNotFoundException extends Exception {

    public PlaylistNotFoundException() {
        super();
    }

    public PlaylistNotFoundException(String message) {
        super(message);
    }

    public PlaylistNotFoundException(String message, Throwable throwable) {
        super(throwable);
    }
}
