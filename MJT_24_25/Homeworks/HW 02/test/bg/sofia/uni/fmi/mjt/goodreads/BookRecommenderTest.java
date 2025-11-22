package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.BookRecommender;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookRecommenderTest {

    @Test
    void testRecommendBooks() {
        Set<Book> books = Set.of(
            new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1"),
            new Book("2", "Book Two", "Author B", "Description B", List.of("Genre B"), 4.0, 200, "url2"),
            new Book("3", "Book Three", "Author C", "Description C", List.of("Genre A", "Genre B"), 4.7, 150, "url3")
        );

        SimilarityCalculator calculator = mock(SimilarityCalculator.class);
        when(calculator.calculateSimilarity(any(Book.class), any(Book.class))).thenReturn(0.5);

        Book originBook = new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1");
        BookRecommender recommender = new BookRecommender(books, calculator);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(originBook, 2);

        assertEquals(2, recommendations.size(), "Expected 2 recommended books");
    }

    @Test
    void testRecommendBooksWithMockedBooks() {
        Set<Book> books = Set.of(
            mock(Book.class),
            mock(Book.class),
            mock(Book.class)
        );

        SimilarityCalculator calculator = mock(SimilarityCalculator.class);
        when(calculator.calculateSimilarity(any(Book.class), any(Book.class))).thenReturn(0.5);

        Book originBook = mock(Book.class);

        BookRecommender recommender = new BookRecommender(books, calculator);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(originBook, 2);

        assertEquals(2, recommendations.size(), "Expected two recommended books");
    }

    @Test
    void testFirstRecommendationHasHighestSimilarity() {
        Set<Book> books = Set.of(
            new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1"),
            new Book("2", "Book Two", "Author B", "Description B", List.of("Genre B"), 4.0, 200, "url2"),
            new Book("3", "Book Three", "Author C", "Description C", List.of("Genre A", "Genre B"), 4.7, 150, "url3")
        );

        SimilarityCalculator calculator = mock(SimilarityCalculator.class);
        when(calculator.calculateSimilarity(any(Book.class), eq(new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1"))))
            .thenReturn(0.9);
        when(calculator.calculateSimilarity(any(Book.class), eq(new Book("2", "Book Two", "Author B", "Description B", List.of("Genre B"), 4.0, 200, "url2"))))
            .thenReturn(0.5);
        when(calculator.calculateSimilarity(any(Book.class), eq(new Book("3", "Book Three", "Author C", "Description C", List.of("Genre A", "Genre B"), 4.7, 150, "url3"))))
            .thenReturn(0.7);

        Book originBook = new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1");
        BookRecommender recommender = new BookRecommender(books, calculator);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(originBook, 3);

        List<Double> similarities = List.copyOf(recommendations.values());
        assertEquals(0.9, similarities.get(0), "First recommendation should have highest similarity");
    }

    @Test
    void testSecondRecommendationHasSecondHighestSimilarity() {
        Set<Book> books = Set.of(
            new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1"),
            new Book("2", "Book Two", "Author B", "Description B", List.of("Genre B"), 4.0, 200, "url2"),
            new Book("3", "Book Three", "Author C", "Description C", List.of("Genre A", "Genre B"), 4.7, 150, "url3")
        );

        SimilarityCalculator calculator = mock(SimilarityCalculator.class);
        when(calculator.calculateSimilarity(any(Book.class), eq(new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1"))))
            .thenReturn(0.9);
        when(calculator.calculateSimilarity(any(Book.class), eq(new Book("2", "Book Two", "Author B", "Description B", List.of("Genre B"), 4.0, 200, "url2"))))
            .thenReturn(0.5);
        when(calculator.calculateSimilarity(any(Book.class), eq(new Book("3", "Book Three", "Author C", "Description C", List.of("Genre A", "Genre B"), 4.7, 150, "url3"))))
            .thenReturn(0.7);

        Book originBook = new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1");
        BookRecommender recommender = new BookRecommender(books, calculator);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(originBook, 3);

        List<Double> similarities = List.copyOf(recommendations.values());
        assertEquals(0.7, similarities.get(1), "Second recommendation should have second highest similarity");
    }

    @Test
    void testThirdRecommendationHasLowestSimilarity() {
        Set<Book> books = Set.of(
            new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1"),
            new Book("2", "Book Two", "Author B", "Description B", List.of("Genre B"), 4.0, 200, "url2"),
            new Book("3", "Book Three", "Author C", "Description C", List.of("Genre A", "Genre B"), 4.7, 150, "url3")
        );

        SimilarityCalculator calculator = mock(SimilarityCalculator.class);
        when(calculator.calculateSimilarity(any(Book.class), eq(new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1"))))
            .thenReturn(0.9);
        when(calculator.calculateSimilarity(any(Book.class), eq(new Book("2", "Book Two", "Author B", "Description B", List.of("Genre B"), 4.0, 200, "url2"))))
            .thenReturn(0.5);
        when(calculator.calculateSimilarity(any(Book.class), eq(new Book("3", "Book Three", "Author C", "Description C", List.of("Genre A", "Genre B"), 4.7, 150, "url3"))))
            .thenReturn(0.7);

        Book originBook = new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1");
        BookRecommender recommender = new BookRecommender(books, calculator);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(originBook, 3);

        List<Double> similarities = List.copyOf(recommendations.values());
        assertEquals(0.5, similarities.get(2), "Third recommendation should have lowest similarity");
    }

    @Test
    void testRecommendBooksWithNoBooks() {
        Set<Book> emptyBooks = Set.of();
        SimilarityCalculator calculator = mock(SimilarityCalculator.class);
        Book originBook = new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1");

        BookRecommender recommender = new BookRecommender(emptyBooks, calculator);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(originBook, 3);
        assertEquals(0, recommendations.size(), "Expected no recommendations when there are no books");
    }

    @Test
    void testRecommendBooksWithNullOriginBook() {
        Set<Book> books = Set.of(
            new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1")
        );

        SimilarityCalculator calculator = mock(SimilarityCalculator.class);
        BookRecommender recommender = new BookRecommender(books, calculator);

        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(null, 3),
            "Expected IllegalArgumentException for null origin book");
    }

    @Test
    void testRecommendBooksWithZeroSimilarity() {
        Set<Book> books = Set.of(
            new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1"),
            new Book("2", "Book Two", "Author B", "Description B", List.of("Genre B"), 4.0, 200, "url2")
        );

        SimilarityCalculator calculator = mock(SimilarityCalculator.class);
        when(calculator.calculateSimilarity(any(Book.class), any(Book.class))).thenReturn(0.0);

        Book originBook = new Book("1", "Book One", "Author A", "Description A", List.of("Genre A"), 4.5, 100, "url1");
        BookRecommender recommender = new BookRecommender(books, calculator);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(originBook, 2);
        assertEquals(0.0, recommendations.values().iterator().next(), "Expected similarity score to be 0");
    }
}
