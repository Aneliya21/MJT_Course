package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.match;

import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity.Candidate;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity.JobPosting;

import static Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.PlatformStatistics.MAX_SIMILARITY_SCORE;
import static Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.PlatformStatistics.MIN_SIMILARITY_SCORE;

public class CandidateJobMatch implements Comparable<CandidateJobMatch> {

    private Candidate candidate;
    private JobPosting jobPosting;
    private double similarityScore;

    public CandidateJobMatch(Candidate candidate, JobPosting jobPosting, double similarityScore) {
        validateInput(similarityScore);
        this.candidate = candidate;
        this.jobPosting = jobPosting;
        this.similarityScore = similarityScore;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    @Override
    public int compareTo(CandidateJobMatch other) {
        int result = Double.compare(other.similarityScore, this.similarityScore);
        if (result != 0) {
            return result;
        }

        return this.candidate.getName().compareTo(other.candidate.getName());
    }

    private static void validateInput(double similarityScore) {
        if (similarityScore < MIN_SIMILARITY_SCORE || similarityScore > MAX_SIMILARITY_SCORE) {
            throw new IllegalArgumentException("SimilarityScore cannot be less that: " + MIN_SIMILARITY_SCORE +
                " and more than: " + MAX_SIMILARITY_SCORE);
        }
    }
}
