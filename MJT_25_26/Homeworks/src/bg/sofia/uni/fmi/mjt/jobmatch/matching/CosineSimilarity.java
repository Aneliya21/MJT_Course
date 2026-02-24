package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.matching;

import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity.Skill;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CosineSimilarity implements SimilarityStrategy {

    public CosineSimilarity() {
        super();
    }

    @Override
    public double calculateSimilarity(Set<Skill> candidateSkills, Set<Skill> jobSkills) {
        validateSkills(candidateSkills, jobSkills);

        if (candidateSkills.isEmpty() && jobSkills.isEmpty()) {
            return 0;
        }

        Set<Skill> sortedCandidateSkills = new TreeSet<>(candidateSkills);
        Set<Skill> sortedJobSkills = new TreeSet<>(jobSkills);

        int product = dotProduct(sortedCandidateSkills, sortedJobSkills);
        double magnitudeOfCandidateSkills = magnitude(sortedCandidateSkills);
        double magnitudeOfJobSkills = magnitude(sortedJobSkills);

        return product / (magnitudeOfCandidateSkills * magnitudeOfJobSkills);
    }

    private static void validateSkills(Set<Skill> candidateSkills, Set<Skill> jobSkills) {
        if (candidateSkills == null) {
            throw new IllegalArgumentException("CandidateSkills cannot be null");
        }
        if (jobSkills == null) {
            throw new IllegalArgumentException("JobSkills cannot be null");
        }
    }

    private int dotProduct(Set<Skill> candidateSkills, Set<Skill> jobSkills) {
        Map<String, Integer> levels = new HashMap<>();

        for (Skill s : jobSkills) {
            levels.put(s.name(), s.level());
        }

        int sum = 0;

        for (Skill s : candidateSkills) {
            Integer lvl = levels.get(s.name());
            if (lvl != null) {
                sum += s.level() * lvl;
            }
        }
        return sum;
    }

    private double magnitude(Set<Skill> skills) {
        int sum = 0;

        for (Skill s : skills) {
            sum += s.level() * s.level();
        }

        return Math.sqrt(sum);
    }
}
