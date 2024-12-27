package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BookRecommender implements BookRecommenderAPI {

    private class ValueComparator implements Comparator<Book> {

        private Map<Book, Double> mp;

        public ValueComparator(Map<Book, Double> mp) {
            this.mp = mp;
        }

        @Override
        public int compare(Book o1, Book o2) {
            if (Double.compare(mp.get(o1), mp.get(o2)) == 0) {
                return -1;
            }
            return Double.compare(mp.get(o2), mp.get(o1));
        }
    }

    private Set<Book> books;
    private SimilarityCalculator similarityCalculator;

    public BookRecommender(Set<Book> initialBooks, SimilarityCalculator calculator) {
        if (initialBooks == null || calculator == null) {
            throw new IllegalArgumentException("Books and similarity calculator cannot be null");
        }
        books = initialBooks;
        similarityCalculator = calculator;
    }

    @Override
    public SortedMap<Book, Double> recommendBooks(Book origin, int maxN) {
        if (origin == null) {
            throw new IllegalArgumentException("Origin cannot be null");
        }
        if (maxN <= 0) {
            throw new IllegalArgumentException("Max number of books cannot be negative or zero");
        }
        Map<Book, Double> calculatedSimilarityMap = books.stream()
            .collect(Collectors.toMap(
                book -> book,
                book -> similarityCalculator.calculateSimilarity(origin, book)
            ));
        SortedMap<Book, Double> sortedMap = new TreeMap<>(new ValueComparator(calculatedSimilarityMap));
        Map<Book, Double> limitedMap = calculatedSimilarityMap.entrySet().stream()
            .limit(maxN)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
        sortedMap.putAll(limitedMap);
        return sortedMap;
    }
}
