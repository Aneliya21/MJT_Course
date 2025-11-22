package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testOfCreatesBookCorrectly() {
        String[] tokens = {
            "1", "Title One", "Author A", "Description of book one",
            "[Fantasy, Adventure]", "4.5", "100", "url1"
        };

        Book book = Book.of(tokens);

        assertEquals("1", book.ID(), "ID is incorrect");
        assertEquals("Title One", book.title(), "Title is incorrect");
        assertEquals("Author A", book.author(), "Author is incorrect");
        assertEquals("Description of book one", book.description(), "Description is incorrect");
        assertEquals(List.of("Fantasy", "Adventure"), book.genres(), "Genres are incorrect");
        assertEquals(4.5, book.rating(), "Rating is incorrect");
        assertEquals(100, book.ratingCount(), "Rating count is incorrect");
        assertEquals("url1", book.URL(), "URL is incorrect");
    }

    @Test
    void testOfThrowsExceptionForInvalidTokensLength() {
        String[] tokens = {"1", "Title One", "Author A"}; // Missing fields

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> Book.of(tokens),
            "Expected exception for invalid tokens length"
        );

        assertTrue(ex.getMessage().contains("Invalid tokens array"));
    }

    @Test
    void testOfThrowsExceptionForInvalidNumberFormat() {
        String[] tokens = {
            "1", "Title One", "Author A", "Description of book one",
            "[Fantasy, Adventure]", "invalid", "100", "url1"
        };

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> Book.of(tokens),
            "Expected exception for invalid number format"
        );

        assertTrue(ex.getMessage().contains("Invalid number format"));
    }

    @Test
    void testFieldsAreSetCorrectly() {
        Book book = new Book("1", "Title", "Author", "Description", List.of("Genre1", "Genre2"), 4.8, 120, "url");

        assertEquals("1", book.ID(), "ID is incorrect");
        assertEquals("Title", book.title(), "Title is incorrect");
        assertEquals("Author", book.author(), "Author is incorrect");
        assertEquals("Description", book.description(), "Description is incorrect");
        assertEquals(List.of("Genre1", "Genre2"), book.genres(), "Genres are incorrect");
        assertEquals(4.8, book.rating(), "Rating is incorrect");
        assertEquals(120, book.ratingCount(), "Rating count is incorrect");
        assertEquals("url", book.URL(), "URL is incorrect");
    }
}
