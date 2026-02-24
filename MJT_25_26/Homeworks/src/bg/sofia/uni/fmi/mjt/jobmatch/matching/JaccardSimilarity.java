package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.matching;

import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity.Skill;

import java.util.HashSet;
import java.util.Set;

public class JaccardSimilarity implements SimilarityStrategy {

    public JaccardSimilarity() {
        super();
    }

    @Override
    public double calculateSimilarity(Set<Skill> candidateSkills, Set<Skill> jobSkills) {
        validateSkills(candidateSkills, jobSkills);

        if (candidateSkills.isEmpty() && jobSkills.isEmpty()) {
            return 0;
        }

        double sizeOfIntersection = findIntersectionOfSkills(candidateSkills, jobSkills).size();
        double sizeOfUnion = findUnionOfSkills(candidateSkills, jobSkills).size();

        return sizeOfIntersection / sizeOfUnion;
    }

    private static void validateSkills(Set<Skill> candidateSkills, Set<Skill> jobSkills) {
        if (candidateSkills == null) {
            throw new IllegalArgumentException("CandidateSkills cannot be null");
        }
        if (jobSkills == null) {
            throw new IllegalArgumentException("JobSkills cannot be null");
        }
    }

    private Set<Skill> findIntersectionOfSkills(Set<Skill> candidateSkills, Set<Skill> jobSkills) {
        Set<Skill> result = new HashSet<>();

        if (candidateSkills.size() < jobSkills.size()) {
            for (Skill skill : jobSkills) {
                if (candidateSkills.contains(skill)) {
                    result.add(skill);
                }
            }
        } else {
            for (Skill skill : candidateSkills) {
                if (jobSkills.contains(skill)) {
                    result.add(skill);
                }
            }
        }
        return result;
    }

    private Set<Skill> findUnionOfSkills(Set<Skill> candidateSkills, Set<Skill> jobSkills) {
        Set<Skill> result = new HashSet<>();
        for (Skill skill : candidateSkills) {
            result.add(skill);
        }
        for (Skill skill : jobSkills) {
            result.add(skill);
        }
        return result;
    }
}

