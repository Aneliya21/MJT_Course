package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookLoaderTest {

    @Test
    void testLoadWithInvalidData() {
        String csvData = """
                ID,Title,Author,Description,Genres,Rating,RatingCount,URL
                1,Title One,Author A,Description A,[Fantasy, Adventure],INVALID,100,url1
                """;

        assertThrows(IllegalArgumentException.class, () ->
            BookLoader.load(new StringReader(csvData)), "Expected exception for invalid number format");
    }

    @Test
    void testLoadEmptyFile() {
        String csvData = """
                ID,Title,Author,Description,Genres,Rating,RatingCount,URL
                """;

        Set<Book> books = BookLoader.load(new StringReader(csvData));
        assertTrue(books.isEmpty(), "Expected no books to be loaded from an empty file");
    }

}
