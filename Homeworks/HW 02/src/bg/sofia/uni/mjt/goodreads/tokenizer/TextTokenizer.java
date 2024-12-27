package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TextTokenizer {

    private final Set<String> stopWords;

    public TextTokenizer(Reader stopwordsReader) {
        try (var br = new BufferedReader(stopwordsReader)) {
            stopWords = br.lines()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load stopwords", ex);
        }
    }

    public List<String> tokenize(String input) {
        if (input == null || input.isBlank()) {
            return List.of();
        }
        String cleaned = input.toLowerCase()
            .replaceAll("\\p{Punct}", "")
            .replaceAll("\\s+", " ");
        return List.of(cleaned.split(" "))
            .stream()
            .filter(word -> !word.isBlank() && !stopWords.contains(word))
            .collect(Collectors.toList());
    }

    public Set<String> stopwords() {
        return stopWords;
    }
}
