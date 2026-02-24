package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class LowAttributeSumEliminationRule implements EliminationRule {

    private int threshold;

    public LowAttributeSumEliminationRule(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        if (ergenkas == null) {
            return null;
        }

        int removedCount = getRemovedCount(ergenkas);
        return buildResultArray(ergenkas, removedCount);
    }

    private int getRemovedCount(Ergenka[] ergenkas) {
        int removed = 0;
        for (Ergenka e : ergenkas) {
            if (e == null) continue;
            if (shouldRemove(e)) removed++;
        }
        return removed;
    }

    private Ergenka[] buildResultArray(Ergenka[] ergenkas, int removedCount) {
        Ergenka[] result = new Ergenka[ergenkas.length - removedCount];
        int ri = 0;

        for (Ergenka e : ergenkas) {
            if (e == null) {
                result[ri++] = null;
            } else {
                if (!shouldRemove(e)) {
                    result[ri++] = e;
                }
            }
            if (ri == result.length) break;
        }

        if (ri != result.length) {
            Ergenka[] trimmed = new Ergenka[ri];
            System.arraycopy(result, 0, trimmed, 0, ri);
            return trimmed;
        }

        return result;
    }

    private boolean shouldRemove(Ergenka e) {
        int currSum = e.getHumorLevel() + e.getRomanceLevel();
        return currSum < this.threshold;
    }
}
