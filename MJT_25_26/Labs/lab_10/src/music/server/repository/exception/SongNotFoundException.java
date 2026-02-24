package Labs.lab_10.src.music.server.repository.exception;

public class SongNotFoundException extends Exception {

    public SongNotFoundException() {
        super();
    }

    public SongNotFoundException(String message) {
        super(message);
    }

    public SongNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

