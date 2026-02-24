package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class LowestRatingEliminationRule implements EliminationRule {

    public LowestRatingEliminationRule() {

    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        if (ergenkas == null) {
            return null;
        }
        if (ergenkas.length == 0) {
            return new Ergenka[0];
        }

        Integer minRating = null;
        int nonNullCount = 0;
        for (Ergenka e : ergenkas) {
            if (e == null) continue;
            nonNullCount++;
            int r = e.getRating();
            if (minRating == null || r < minRating) {
                minRating = r;
            }
        }

        if (nonNullCount == 0) {
            return ergenkas.clone();
        }

        int removedCount = 0;
        for (Ergenka e : ergenkas) {
            if (e == null) continue;
            if (e.getRating() == minRating) removedCount++;
        }

        Ergenka[] result = new Ergenka[ergenkas.length - removedCount];
        int resultIndex = 0;
        for (Ergenka e : ergenkas) {
            if (e == null) {
                result[resultIndex++] = null;
            } else {
                if (e.getRating() != minRating) {
                    result[resultIndex++] = e;
                }
            }
        }
        if (resultIndex != result.length) {
            Ergenka[] trimmed = new Ergenka[resultIndex];
            System.arraycopy(result, 0, trimmed, 0, resultIndex);
            return trimmed;
        }

        return result;
    }
}