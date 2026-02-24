package Labs.lab_10.src.music.server.repository.exception;

public class SongAlreadyExistsException extends Exception {

    public SongAlreadyExistsException() {
        super();
    }

    public SongAlreadyExistsException(String message) {
        super(message);
    }

    public SongAlreadyExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}