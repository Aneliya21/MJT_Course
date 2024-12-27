package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextTokenizerTest {

    private TextTokenizer tokenizer;

    @BeforeEach
    void setUp() throws IOException {
        Path stopwordsFilePath = Path.of("stopwords.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(stopwordsFilePath.toFile()))) {
            tokenizer = new TextTokenizer(reader);
        }
    }

    @Test
    void testTokenizeWithValidInput() {
        String input = "This is an example of a text with, punctuation! Isn't it amazing?";
        List<String> tokens = tokenizer.tokenize(input);

        List<String> expected = List.of("example", "text", "punctuation", "amazing");
        assertEquals(expected, tokens, "The tokenized output does not match the expected result");
    }

    @Test
    void testTokenizeWithEmptyInput() {
        String input = "";
        List<String> tokens = tokenizer.tokenize(input);

        assertEquals(List.of(), tokens, "Tokenizing an empty string should return an empty list");
    }

    @Test
    void testTokenizeWithOnlyStopwords() {
        String input = "a about above after again against all and i it its";
        List<String> tokens = tokenizer.tokenize(input);

        assertEquals(List.of(), tokens, "Tokenizing a string with only stopwords should return an empty list");
    }

    @Test
    void testTokenizeWithSpecialCharacters() {
        String input = "Hello, world! This is a test: do you pass?";
        List<String> tokens = tokenizer.tokenize(input);

        List<String> expected = List.of("hello", "world", "test", "pass");
        assertEquals(expected, tokens, "Special characters should be removed during tokenization");
    }

    @Test
    void testTokenizeWithNullInput() {
        String input = null;
        List<String> tokens = tokenizer.tokenize(input);

        assertEquals(List.of(), tokens, "Tokenizing null input should return an empty list");
    }

    @Test
    void testTokenizeWithoutStopwords() {
        String input = "Programming is fun and challenging";
        List<String> tokens = tokenizer.tokenize(input);

        List<String> expected = List.of("programming", "fun", "challenging");
        assertEquals(expected, tokens, "All non-stopword tokens should be returned");
    }

    @Test
    void testTokenizeWithOnlyPunctuation() {
        String input = "!!!...,,,???";
        List<String> tokens = tokenizer.tokenize(input);

        assertEquals(List.of(), tokens, "Tokenizing only punctuation should return an empty list");
    }

    @Test
    void testTokenizeWithVariousWhitespace() {
        String input = "This\tis\na    test with\n\nmultiple\t\tspaces.";
        List<String> tokens = tokenizer.tokenize(input);

        List<String> expected = List.of("test", "multiple", "spaces");
        assertEquals(expected, tokens, "Whitespace variations should be normalized during tokenization");
    }

    @Test
    void testTokenizeWithSpecialSymbolsInWords() {
        String input = "It's a programmer's life!";
        List<String> tokens = tokenizer.tokenize(input);

        List<String> expected = List.of("programmers", "life");
        assertEquals(expected, tokens, "Words with special symbols should be tokenized correctly");
    }
}
