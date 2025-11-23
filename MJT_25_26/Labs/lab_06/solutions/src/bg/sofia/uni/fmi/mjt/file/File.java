package bg.sofia.uni.fmi.mjt.file;

import bg.sofia.uni.fmi.mjt.file.exception.EmptyFileException;

import java.util.Objects;

/**
 * A simple in-memory representation of a file containing textual content.
 */
public class File {

    private String content;

    /**
     * Creates a new File with the given content.
     *
     * @param content the initial content of the file
     * @throws IllegalArgumentException if content is null
     */
    public File(String content) {
        setContent(content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null");
        }

        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File)) return false;
        File other = (File) o;
        return Objects.equals(this.getContent(), other.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContent());
    }

}