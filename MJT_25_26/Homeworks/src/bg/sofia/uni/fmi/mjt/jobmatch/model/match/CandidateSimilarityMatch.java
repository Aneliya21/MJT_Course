package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.match;

import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity.Candidate;

import static Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.PlatformStatistics.MAX_SIMILARITY_SCORE;
import static Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.PlatformStatistics.MIN_SIMILARITY_SCORE;

public class CandidateSimilarityMatch implements Comparable<CandidateSimilarityMatch> {

    private Candidate targetCandidate;
    private Candidate similarCandidate;
    private double similarityScore;

    public CandidateSimilarityMatch(Candidate targetCandidate, Candidate similarCandidate, double similarityScore) {
        validateInput(targetCandidate, similarCandidate, similarityScore);
        this.targetCandidate = targetCandidate;
        this.similarCandidate = similarCandidate;
        this.similarityScore = similarityScore;
    }

    public Candidate getTargetCandidate() {
        return targetCandidate;
    }

    public Candidate getSimilarCandidate() {
        return similarCandidate;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    @Override
    public int compareTo(CandidateSimilarityMatch other) {
        int result = Double.compare(other.similarityScore, this.similarityScore);
        if (result != 0) {
            return result;
        }

        return this.targetCandidate.getName().compareTo(other.targetCandidate.getName());
    }

    private static void validateInput(Candidate targetCandidate, Candidate similarCandidate, double similarityScore) {
        if (targetCandidate == null) {
            throw new IllegalArgumentException("TargetCandidate cannot be null");
        }
        if (similarCandidate == null) {
            throw new IllegalArgumentException("SimilarCandidate cannot be null");
        }
        if (similarityScore < MIN_SIMILARITY_SCORE || similarityScore > MAX_SIMILARITY_SCORE) {
            throw new IllegalArgumentException("SimilarityScore cannot be less that: " + MIN_SIMILARITY_SCORE +
                " and more than: " + MAX_SIMILARITY_SCORE);
        }
    }

}
