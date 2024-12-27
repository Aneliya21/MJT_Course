package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashSet;
import java.util.Set;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Books cannot be null");
        }
        Set<String> a = new HashSet<>(first.genres());
        Set<String> b = new HashSet<>(second.genres());

        a.retainAll(b);

        int intersectionSize = a.size();
        int smallerSetSize = Math.min(first.genres().size(), second.genres().size());

        if (smallerSetSize == 0) {
            return 0.0;
        }
        return (double) intersectionSize / smallerSetSize;
    }
}
