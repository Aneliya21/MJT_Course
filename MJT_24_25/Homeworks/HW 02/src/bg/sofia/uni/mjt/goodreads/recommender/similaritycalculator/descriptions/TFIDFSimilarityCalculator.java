package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {

    private final Set<Book> books;
    private final TextTokenizer tokenizer;
    private final Map<String, Double> idfCache;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        this.books = books;
        this.tokenizer = tokenizer;
        this.idfCache = computeIDF();
    }

    /*
     * Do not modify!
     */
    @Override
    public double calculateSimilarity(Book first, Book second) {
        Map<String, Double> tfIdfScoresFirst = computeTFIDF(first);
        Map<String, Double> tfIdfScoresSecond = computeTFIDF(second);

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    public Map<String, Double> computeTFIDF(Book book) {
        Map<String, Double> tfScores = computeTF(book);

        return tfScores.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue() * idfCache.getOrDefault(entry.getKey(), 0.0)
            ));
    }

    public Map<String, Double> computeTF(Book book) {
        List<String> words = tokenizer.tokenize(book.description().toLowerCase());

        Map<String, Long> wordCounts = words.stream()
            .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        long maxFrequency = wordCounts.values().stream().max(Long::compareTo).orElse(1L);

        return wordCounts.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> (double) entry.getValue() / maxFrequency
            ));
    }

    public Map<String, Double> computeIDF() {
        Map<String, Integer> documentFrequencies = new HashMap<>();
        int totalDocuments = books.size();

        for (Book book : books) {
            Set<String> uniqueWords = new HashSet<>(tokenizer.tokenize(book.description().toLowerCase()));
            for (String word : uniqueWords) {
                documentFrequencies.put(word, documentFrequencies.getOrDefault(word, 0) + 1);
            }
        }

        return documentFrequencies.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> Math.log((double) totalDocuments / entry.getValue())
            ));
    }

    private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
        double magnitudeFirst = magnitude(first.values());
        double magnitudeSecond = magnitude(second.values());

        if (magnitudeFirst == 0 || magnitudeSecond == 0) {
            return 0.0;
        }

        return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
    }

    private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
        Set<String> commonKeys = new HashSet<>(first.keySet());
        commonKeys.retainAll(second.keySet());

        return commonKeys.stream()
            .mapToDouble(word -> first.get(word) * second.get(word))
            .sum();
    }

    private double magnitude(Collection<Double> input) {
        double squaredMagnitude = input.stream()
            .map(v -> v * v)
            .reduce(0.0, Double::sum);

        return Math.sqrt(squaredMagnitude);
    }
}
