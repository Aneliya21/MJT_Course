package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres.GenresOverlapSimilarityCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenresOverlapSimilarityCalculatorTest {

    @Test
    void testCalculateSimilarityWithNullBooksThrowsException() {
        GenresOverlapSimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> calculator.calculateSimilarity(null,
                new Book("1", "Title", "Author", "Description", List.of("Genre A"), 4.5, 100, "url")),
            "Expected IllegalArgumentException for null first book");

        assertEquals("Books cannot be null", thrown.getMessage());

        thrown = assertThrows(IllegalArgumentException.class,
            () -> calculator.calculateSimilarity(
                new Book("1", "Title", "Author", "Description", List.of("Genre A"), 4.5, 100, "url"), null),
            "Expected IllegalArgumentException for null second book");

        assertEquals("Books cannot be null", thrown.getMessage());
    }

    @Test
    void testCalculateSimilarityWithNoGenresReturnsZero() {
        Book first = new Book("1", "Title One", "Author A", "Description A", List.of(), 4.5, 100, "url1");
        Book second = new Book("2", "Title Two", "Author B", "Description B", List.of(), 4.0, 50, "url2");

        GenresOverlapSimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();
        double similarity = calculator.calculateSimilarity(first, second);

        assertEquals(0.0, similarity, "Expected similarity to be 0.0 when both books have no genres");
    }

    @Test
    void testCalculateSimilarityWithNoOverlapReturnsZero() {
        Book first =
            new Book("1", "Title One", "Author A", "Description A", List.of("Genre A", "Genre B"), 4.5, 100, "url1");
        Book second =
            new Book("2", "Title Two", "Author B", "Description B", List.of("Genre C", "Genre D"), 4.0, 50, "url2");

        GenresOverlapSimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();
        double similarity = calculator.calculateSimilarity(first, second);

        assertEquals(0.0, similarity, "Expected similarity to be 0.0 when there is no overlap between genres");
    }

    @Test
    void testCalculateSimilarityWithCompleteOverlapReturnsOne() {
        Book first =
            new Book("1", "Title One", "Author A", "Description A", List.of("Genre A", "Genre B"), 4.5, 100, "url1");
        Book second =
            new Book("2", "Title Two", "Author B", "Description B", List.of("Genre A", "Genre B"), 4.0, 50, "url2");

        GenresOverlapSimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();
        double similarity = calculator.calculateSimilarity(first, second);

        assertEquals(1.0, similarity, "Expected similarity to be 1.0 when all genres overlap");
    }

    @Test
    void testCalculateSimilarityWithPartialOverlap() {
        Book first =
            new Book("1", "Title One", "Author A", "Description A", List.of("Genre A", "Genre B", "Genre C"), 4.5, 100,
                "url1");
        Book second =
            new Book("2", "Title Two", "Author B", "Description B", List.of("Genre B", "Genre C", "Genre D"), 4.0, 50,
                "url2");

        GenresOverlapSimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();
        double similarity = calculator.calculateSimilarity(first, second);

        double expected = 2.0 / 3.0;
        assertEquals(expected, similarity, 0.0001,
            "Expected similarity to be 2/3 when two out of three genres overlap");
    }
}
