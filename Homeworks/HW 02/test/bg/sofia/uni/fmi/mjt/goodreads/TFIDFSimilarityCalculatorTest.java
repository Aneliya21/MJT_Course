package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions.TFIDFSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TFIDFSimilarityCalculatorTest {

    private TFIDFSimilarityCalculator calculator;
    private TextTokenizer tokenizer;
    private Set<Book> books;

    @BeforeEach
    void setUp() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("stopwords.txt"))) {
            tokenizer = new TextTokenizer(reader);
        }

        books = Set.of(
            new Book("1", "Book One", "Author A", "Magic adventure with wizards", List.of("Fantasy", "Adventure"), 4.5,
                100, "url1"),
            new Book("2", "Book Two", "Author B", "Adventure with dragons and magic spells",
                List.of("Fantasy", "Adventure"), 4.7, 200, "url2")
        );

        calculator = new TFIDFSimilarityCalculator(books, tokenizer);
    }

    @Test
    void testComputeTF() {
        Book book =
            new Book("1", "Book One", "Author A", "Magic magic adventure with wizards", List.of("Fantasy", "Adventure"),
                4.5, 100, "url1");
        Map<String, Double> tf = calculator.computeTF(book);

        assertEquals(1.0, tf.get("magic"), "The term frequency for 'magic' should be 1.0");
        assertEquals(0.5, tf.get("adventure"), "The term frequency for 'adventure' should be 0.5");
        assertEquals(0.5, tf.get("wizards"), "The term frequency for 'wizards' should be 0.5");
    }

    @Test
    void testComputeIDF() {
        Map<String, Double> idf = calculator.computeIDF();

        assertEquals(Math.log(2.0 / 2), idf.get("magic"), "The IDF for 'magic' should be 0.0");
        assertEquals(Math.log(2.0 / 2), idf.get("adventure"), "The IDF for 'adventure' should be 0.0");
    }

    @Test
    void testComputeTFIDF() {
        Book book =
            new Book("1", "Book One", "Author A", "Magic adventure with wizards", List.of("Fantasy", "Adventure"), 4.5,
                100, "url1");
        Map<String, Double> tfIdf = calculator.computeTFIDF(book);

        assertEquals(0.25 * Math.log(2.0 / 2), tfIdf.get("magic"));
        assertEquals(0.25 * Math.log(2.0 / 2), tfIdf.get("adventure"));
    }

    @Test
    void testCalculateSimilarity() {
        Book book1 =
            new Book("1", "Book One", "Author A", "Magic adventure with wizards", List.of("Fantasy", "Adventure"), 4.5,
                100, "url1");
        Book book2 = new Book("2", "Book Two", "Author B", "Adventure with dragons and magic spells",
            List.of("Fantasy", "Adventure"), 4.7, 200, "url2");

        double similarity = calculator.calculateSimilarity(book1, book2);

        System.out.println("Similarity: " + similarity);
    }
}
