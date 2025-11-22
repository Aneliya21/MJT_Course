package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite.CompositeSimilarityCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CompositeSimilarityCalculatorTest {

    @Test
    void testConstructorWithNullMapThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> new CompositeSimilarityCalculator(null),
            "Expected IllegalArgumentException for null map");

        assertEquals("The similarity calculator map cannot be null or empty", thrown.getMessage());
    }

    @Test
    void testConstructorWithEmptyMapThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> new CompositeSimilarityCalculator(Map.of()),
            "Expected IllegalArgumentException for empty map");

        assertEquals("The similarity calculator map cannot be null or empty", thrown.getMessage());
    }

    @Test
    void testCalculateSimilarityWithNullBooksThrowsException() {
        SimilarityCalculator mockCalculator = mock(SimilarityCalculator.class);
        CompositeSimilarityCalculator compositeCalculator = new CompositeSimilarityCalculator(Map.of(mockCalculator, 1.0));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> compositeCalculator.calculateSimilarity(null, new Book("1", "Title", "Author", "Description", List.of("Genre"), 4.5, 100, "url")),
            "Expected IllegalArgumentException for null first book");

        assertEquals("Books cannot be null", thrown.getMessage());

        thrown = assertThrows(IllegalArgumentException.class,
            () -> compositeCalculator.calculateSimilarity(new Book("1", "Title", "Author", "Description", List.of("Genre"), 4.5, 100, "url"), null),
            "Expected IllegalArgumentException for null second book");

        assertEquals("Books cannot be null", thrown.getMessage());
    }

    @Test
    void testCalculateSimilarityWithValidInputs() {
        Book first = new Book("1", "Title One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1");
        Book second = new Book("2", "Title Two", "Author B", "Description B", List.of("Genre B"), 4.0, 50, "url2");

        SimilarityCalculator mockCalculator1 = mock(SimilarityCalculator.class);
        SimilarityCalculator mockCalculator2 = mock(SimilarityCalculator.class);

        when(mockCalculator1.calculateSimilarity(first, second)).thenReturn(0.8);
        when(mockCalculator2.calculateSimilarity(first, second)).thenReturn(0.6);

        Map<SimilarityCalculator, Double> calculatorMap = Map.of(
            mockCalculator1, 0.7,
            mockCalculator2, 0.3
        );

        CompositeSimilarityCalculator compositeCalculator = new CompositeSimilarityCalculator(calculatorMap);

        double result = compositeCalculator.calculateSimilarity(first, second);

        double expected = (0.8 * 0.7) + (0.6 * 0.3);
        assertEquals(expected, result, 0.0001, "The calculated similarity is not as expected");

        verify(mockCalculator1).calculateSimilarity(first, second);
        verify(mockCalculator2).calculateSimilarity(first, second);
    }
}
