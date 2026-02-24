package Labs.lab_10.src.music.server.model;

import java.util.Objects;

public record Song(String title, String artist, int duration) {

    public Song {
        validateStringValue(title, "Title");
        validateStringValue(artist, "Artist");
        validateIntValue(duration, "Duration");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(title, song.title) &&
            Objects.equals(artist, song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }

    private static void validateStringValue(String value, String fieldName) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    private static void validateIntValue(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be a positive integer");
        }
    }
}