package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class PublicVoteEliminationRule implements EliminationRule {

    private String[] votes;

    public PublicVoteEliminationRule(String[] votes) {
        this.votes = votes;
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        if (ergenkas == null) {
            return null;
        }
        if (isVotesEmpty()) {
            return ergenkas;
        }

        String candidate = findCandidate();
        if (candidate == null) {
            return ergenkas;
        }

        if (!hasMajority(candidate)) {
            return ergenkas;
        }

        Ergenka eliminated = findEliminated(ergenkas, candidate);
        if (eliminated == null) {
            return ergenkas;
        }

        return buildResultArray(ergenkas, eliminated);
    }

    private boolean isVotesEmpty() {
        return this.votes == null || this.votes.length == 0;
    }

    private String findCandidate() {
        String candidate = null;
        int count = 0;

        for (String vote : this.votes) {
            if (count == 0) {
                candidate = vote;
                count = 1;
            } else if (candidate == null ? vote == null : candidate.equals(vote)) {
                count++;
            } else {
                count--;
            }
        }

        return candidate;
    }

    private boolean hasMajority(String candidate) {
        int actualCount = 0;
        for (String vote : this.votes) {
            if (candidate == null) {
                if (vote == null) actualCount++;
            } else if (vote != null && vote.equals(candidate)) {
                actualCount++;
            }
        }
        return actualCount > (this.votes.length / 2);
    }

    private Ergenka findEliminated(Ergenka[] ergenkas, String candidate) {
        if (ergenkas == null) return null;
        for (Ergenka e : ergenkas) {
            if (e == null) continue;
            String name = e.getName();
            if (candidate == null) {
                if (name == null) return e;
            } else if (name != null && candidate.equals(name)) {
                return e;
            }
        }
        return null;
    }

    private Ergenka[] buildResultArray(Ergenka[] ergenkas, Ergenka eliminated) {
        int removedCount = 1;
        Ergenka[] result = new Ergenka[ergenkas.length - removedCount];
        int ri = 0;
        for (Ergenka e : ergenkas) {
            if (e == null) {
                // preserve nulls from input
                result[ri++] = null;
            } else {
                if (!e.equals(eliminated)) {
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
}
