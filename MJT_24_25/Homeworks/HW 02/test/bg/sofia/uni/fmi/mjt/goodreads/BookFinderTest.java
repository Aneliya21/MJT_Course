package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.finder.BookFinder;
import bg.sofia.uni.fmi.mjt.goodreads.finder.MatchOption;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookFinderTest {

    private BookFinder bookFinder;

    @BeforeEach
    void setUp() {
        Set<Book> books = Set.of(
            new Book("1", "Title One", "Author A", "Description of book one", List.of("Fantasy", "Adventure"), 4.5, 100, "url1"),
            new Book("2", "Title Two", "Author B", "Description of book two", List.of("Mystery", "Thriller"), 4.0, 200, "url2"),
            new Book("3", "Title Three", "Author A", "Another description", List.of("Fantasy", "Drama"), 4.7, 150, "url3")
        );
        try (BufferedReader reader = new BufferedReader(new FileReader("stopwords.txt"))) {
            TextTokenizer tokenizer = new TextTokenizer(reader);
            bookFinder = new BookFinder(books, tokenizer);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAllBooks() {
        Set<Book> allBooks = bookFinder.allBooks();
        assertEquals(3, allBooks.size(), "The number of books is incorrect");
    }

    @Test
    void testSearchByAuthor() {
        List<Book> authorBooks = bookFinder.searchByAuthor("Author A");
        assertEquals(2, authorBooks.size(), "The number of books by Author A is incorrect");

        authorBooks = bookFinder.searchByAuthor("Author B");
        assertEquals(1, authorBooks.size(), "The number of books by Author B is incorrect");

        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(null), "Expected exception for null author name");
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(""), "Expected exception for empty author name");
    }

    @Test
    void testAllGenres() {
        Set<String> allGenres = bookFinder.allGenres();
        assertEquals(Set.of("Fantasy", "Adventure", "Mystery", "Thriller", "Drama"), allGenres, "Genres are incorrect");
    }

    @Test
    void testSearchByGenresMatchAll() {
        Set<String> genres = Set.of("Fantasy", "Adventure");
        List<Book> matchingBooks = bookFinder.searchByGenres(genres, MatchOption.MATCH_ALL);
        assertEquals(1, matchingBooks.size(), "Expected one book with all genres matching");
    }

    @Test
    void testSearchByGenresMatchAny() {
        Set<String> genres = Set.of("Fantasy", "Thriller");
        List<Book> matchingBooks = bookFinder.searchByGenres(genres, MatchOption.MATCH_ANY);
        assertEquals(3, matchingBooks.size(), "Expected three books with any genre matching");
    }

    @Test
    void testSearchByKeywordsMatchAll() {
        Set<String> keywords = Set.of("title", "one");
        List<Book> matchingBooks = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ALL);
        assertEquals(1, matchingBooks.size(), "Expected one book with all keywords matching");
    }

    @Test
    void testSearchByKeywordsMatchAny() {
        Set<String> keywords = Set.of("title", "mystery");
        List<Book> matchingBooks = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ANY);
        assertEquals(3, matchingBooks.size(), "Expected three books with any keyword matching");
    }

    @Test
    void testSearchByKeywordsEmptySet() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByKeywords(Set.of(), MatchOption.MATCH_ALL), "Expected exception for empty keyword set");
    }

    @Test
    void testSearchByNonExistingAuthor() {
        List<Book> authorBooks = bookFinder.searchByAuthor("Non Existing Author");
        assertTrue(authorBooks.isEmpty(), "Expected no books for a non-existing author");
    }

    @Test
    void testSearchByNonExistingGenres() {
        Set<String> genres = Set.of("NonExistingGenre");
        List<Book> matchingBooks = bookFinder.searchByGenres(genres, MatchOption.MATCH_ANY);
        assertTrue(matchingBooks.isEmpty(), "Expected no books for a non-existing genre");
    }
}
