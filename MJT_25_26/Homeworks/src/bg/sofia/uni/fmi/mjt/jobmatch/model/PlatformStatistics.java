package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model;

public record PlatformStatistics(
    int totalCandidates,
    int totalEmployers,
    int totalJobPostings,
    String mostCommonSkillName,
    String highestPaidJobTitle) {

    public static final int MIN_SIMILARITY_SCORE = 0;
    public static final int MAX_SIMILARITY_SCORE = 1;

    public PlatformStatistics {
        validateInput(totalCandidates, totalEmployers, totalJobPostings);
    }

    private static void validateInput(int totalCandidates, int totalEmployers, int totalJobPostings) {
        if (totalCandidates < 0) {
            throw new IllegalArgumentException("TotalCandidates cannot be negative");
        }
        if (totalEmployers < 0) {
            throw new IllegalArgumentException("TotalEmployers cannot be negative");
        }
        if (totalJobPostings < 0) {
            throw new IllegalArgumentException("TotalJobPostings cannot be negative");
        }
    }
}
