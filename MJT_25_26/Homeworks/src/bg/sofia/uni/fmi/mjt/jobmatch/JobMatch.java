package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch;

import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.api.JobMatchAPI;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.exceptions.CandidateNotFoundException;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.exceptions.JobPostingNotFoundException;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.exceptions.UserAlreadyExistsException;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.exceptions.UserNotFoundException;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.matching.CosineSimilarity;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.matching.SimilarityStrategy;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.PlatformStatistics;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity.Candidate;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity.Employer;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity.JobPosting;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity.Skill;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.match.CandidateJobMatch;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.match.CandidateSimilarityMatch;
import Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.match.SkillRecommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JobMatch implements JobMatchAPI {

    private final Set<Candidate> candidates;
    private final Set<Employer> employers;
    private final Set<JobPosting> jobPostings;

    public JobMatch() {
        this.candidates = new HashSet<>();
        this.employers = new HashSet<>();
        this.jobPostings = new HashSet<>();
    }

    public JobMatch(Set<Candidate> candidates, Set<Employer> employers,
                    Set<JobPosting> jobPostings) {
        this.candidates = candidates;
        this.employers = employers;
        this.jobPostings = jobPostings;
    }

    @Override
    public Candidate registerCandidate(Candidate candidate) {
        candidateRegistrationValidation(candidate);

        candidates.add(candidate);
        return candidate;
    }

    @Override
    public Employer registerEmployer(Employer employer) {
        employerRegistrationValidation(employer);

        employers.add(employer);
        return employer;
    }

    @Override
    public JobPosting postJobPosting(JobPosting jobPosting) {
        jobPostingValidation(jobPosting);

        jobPostings.add(jobPosting);
        return jobPosting;
    }

    @Override
    public List<CandidateJobMatch> findTopNCandidatesForJob(String jobPostingId, int limit,
                                                            SimilarityStrategy strategy) {
        jobPostingIdValidation(jobPostingId);
        limitValidation(limit);
        strategyValidation(strategy);

        List<CandidateJobMatch> all = findAllCandidates(jobPostingId, strategy);
        Collections.sort(all);

        int n = Math.min(limit, all.size());
        List<CandidateJobMatch> result = new ArrayList<>(all.subList(0, n));

        return Collections.unmodifiableList(result);
    }

    @Override
    public List<CandidateJobMatch> findTopNJobsForCandidate(String candidateEmail, int limit,
                                                            SimilarityStrategy strategy) {
        candidateEmailValidation(candidateEmail);
        limitValidation(limit);
        strategyValidation(strategy);

        List<CandidateJobMatch> all = findAllJobs(candidateEmail, strategy);
        Collections.sort(all);

        int n = Math.min(limit, all.size());
        List<CandidateJobMatch> result = new ArrayList<>(all.subList(0, n));

        return Collections.unmodifiableList(result);
    }

    @Override
    public List<CandidateSimilarityMatch> findSimilarCandidates(String candidateEmail, int limit,
                                                                SimilarityStrategy strategy) {
        candidateEmailValidation(candidateEmail);
        limitValidation(limit);
        strategyValidation(strategy);

        List<CandidateSimilarityMatch> all = findAllSimilarCandidates(candidateEmail, strategy);

        Collections.sort(all);

        int n = Math.min(limit, all.size());
        return new ArrayList<>(all.subList(0, n));
    }

    @Override
    public List<SkillRecommendation> getSkillRecommendationsForCandidate(String candidateEmail, int limit) {
        candidateEmailValidation(candidateEmail);
        limitValidation(limit);

        Candidate candidate = findCandidateByEmail(candidateEmail);

        SimilarityStrategy similarityStrategy = new CosineSimilarity();
        Map<String, Double> totalImprovement = new HashMap<>();

        for (JobPosting jobPosting : jobPostings) {
            double currentSimilarityScore = similarityStrategy.calculateSimilarity(
                candidate.getSkills(), jobPosting.getRequiredSkills());

            Set<Skill> missingSkills = findMissingSkills(candidate.getSkills(), jobPosting.getRequiredSkills());
            if (missingSkills == null || missingSkills.isEmpty()) {
                continue;
            }

            fillMapOfTotalImprovement(totalImprovement, missingSkills, candidate,
                                        similarityStrategy, currentSimilarityScore, jobPosting);
        }

        List<SkillRecommendation> recommendations = getSkillRecommendationsFromMap(totalImprovement);

        Collections.sort(recommendations);
        int n = Math.min(limit, recommendations.size());

        return new ArrayList<>(recommendations.subList(0, n));
    }

    @Override
    public PlatformStatistics getPlatformStatistics() {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        int totalCandidates = candidates.size();
        int totalEmployers = employers.size();
        int totalJobPostings = jobPostings.size();
        String mostCommonSkillName = findMostCommonSkillName(candidates);
        String highestPaidJobTitle = findHighestPaidJobTitle(jobPostings);

        return new PlatformStatistics(totalCandidates,
            totalEmployers,
            totalJobPostings,
            mostCommonSkillName,
            highestPaidJobTitle);
    }

    private void candidateRegistrationValidation(Candidate candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate cannot be null, empty or blank");
        }
        for (Candidate c : candidates) {
            if (c.getEmail().equals(candidate.getEmail())) {
                throw new UserAlreadyExistsException("Candidate with this email already exists");
            }
        }
    }

    private void employerRegistrationValidation(Employer employer) {
        if (employer == null) {
            throw new IllegalArgumentException("Employer cannot be null");
        }
        for (Employer e : employers) {
            if (e.email().equals(employer.email())) {
                throw new UserAlreadyExistsException("Employer with this email already exists");
            }
        }
    }

    private void jobPostingValidation(JobPosting jobPosting) {
        if (jobPosting == null) {
            throw new IllegalArgumentException("JobPosting cannot be null");
        }
        for (Employer employer : employers) {
            if (employer.email().equals(jobPosting.getEmployerEmail())) {
                return;
            }
        }
        throw new UserNotFoundException("The employer publishing the job posting is not registered");
    }

    private void jobPostingIdValidation(String jobPostingId) {
        if (jobPostingId == null || jobPostingId.isEmpty() || jobPostingId.isBlank()) {
            throw new IllegalArgumentException("JobPostingId cannot be null, empty or blank");
        }
        for (JobPosting j : jobPostings) {
            if (j.getId().equals(jobPostingId)) {
                return;
            }
        }
        throw new JobPostingNotFoundException("JobPostingId does not exist");
    }

    private static void limitValidation(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit cannot be negative or zero");
        }
    }

    private static void strategyValidation(SimilarityStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
    }

    private List<CandidateJobMatch> findAllCandidates(String jobPostingId, SimilarityStrategy strategy) {
        List<CandidateJobMatch> result = new ArrayList<>(candidates.size());
        JobPosting jobPosting = findJobPostingById(jobPostingId);

        for (Candidate c : candidates) {
            double similarity = strategy.calculateSimilarity(c.getSkills(), jobPosting.getRequiredSkills());
            if (similarity > 0) {
                CandidateJobMatch current = new CandidateJobMatch(c, jobPosting, similarity);
                result.add(current);
            }
        }

        return result;
    }

    private Candidate findCandidateByEmail(String email) {
        Candidate candidate = null;
        for (Candidate c : candidates) {
            if (c.getEmail().equals(email)) {
                candidate = c;
                break;
            }
        }
        return candidate;
    }

    private List<CandidateJobMatch> findAllJobs(String candidateEmail, SimilarityStrategy strategy) {
        Candidate candidate = findCandidateByEmail(candidateEmail);
        List<CandidateJobMatch> result = new ArrayList<>();
        for (JobPosting job : jobPostings) {
            double similarity = strategy.calculateSimilarity(candidate.getSkills(), job.getRequiredSkills());
            if (similarity > 0) {
                result.add(new CandidateJobMatch(candidate, job, similarity));
            }
        }

        return result;
    }

    private List<CandidateSimilarityMatch> findAllSimilarCandidates(String candidateEmail,
                                                                    SimilarityStrategy strategy) {
        Candidate target = findCandidateByEmail(candidateEmail);

        List<CandidateSimilarityMatch> result = new ArrayList<>();

        for (Candidate other : candidates) {
            if (other.getEmail().equals(candidateEmail)) {
                continue;
            }

            double similarity = strategy.calculateSimilarity(target.getSkills(), other.getSkills());
            if (similarity > 0) {
                CandidateSimilarityMatch candidateSimilarityMatch =
                    new CandidateSimilarityMatch(target, other, similarity);
                result.add(candidateSimilarityMatch);
            }
        }

        return result;
    }

    private void candidateEmailValidation(String candidateEmail) {
        if (candidateEmail == null || candidateEmail.isEmpty() || candidateEmail.isBlank()) {
            throw new IllegalArgumentException("CandidateEmail cannot be null, empty or blank");
        }
        for (Candidate c : candidates) {
            if (c.getEmail().equals(candidateEmail)) {
                return;
            }
        }
        throw new CandidateNotFoundException("No candidate with this email has been found");
    }

    private JobPosting findJobPostingById(String jobPostingId) {
        for (JobPosting jobPosting : jobPostings) {
            if (jobPosting.getId().equals(jobPostingId)) {
                return jobPosting;
            }
        }
        return null;
    }

    private String findMostCommonSkillName(Set<Candidate> candidates) {
        Map<String, Integer> counts = new HashMap<>();

        for (Candidate candidate : candidates) {
            for (Skill skill : candidate.getSkills()) {
                String name = skill.name();
                counts.put(name, counts.getOrDefault(name, 0) + 1);
            }
        }

        String mostCommon = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            String name = entry.getKey();
            int count = entry.getValue();

            if (count > maxCount) {
                maxCount = count;
                mostCommon = name;
            } else if (count == maxCount) {
                if (mostCommon == null || name.compareTo(mostCommon) < 0) {
                    mostCommon = name;
                }
            }
        }

        return mostCommon;
    }

    private String findHighestPaidJobTitle(Set<JobPosting> jobPostings) {

        List<JobPosting> sortedJobPostings = new ArrayList<>(jobPostings);
        Collections.sort(sortedJobPostings);

        if (!sortedJobPostings.isEmpty()) {
            return sortedJobPostings.get(0).getTitle();
        }

        return null;
    }

    private boolean isMissingSkill(Set<Skill> candidateSkills, Skill requiredSkill) {
        for (Skill skill : candidateSkills) {
            if (skill.equals(requiredSkill)) {
                return false;
            }
        }
        return true;
    }

    private Set<Skill> findMissingSkills(Set<Skill> candidateSkills, Set<Skill> requiredSkills) {
        Set<Skill> result = new HashSet<>();

        for (Skill requiredSkill : requiredSkills) {
            if (isMissingSkill(candidateSkills, requiredSkill)) {
                result.add(requiredSkill);
            }
        }

        return result;
    }

    private List<SkillRecommendation> getSkillRecommendationsFromMap(Map<String, Double> totalImprovement) {
        List<SkillRecommendation> recommendations = new ArrayList<>();
        for (Map.Entry<String, Double> e : totalImprovement.entrySet()) {
            double total = e.getValue();
            if (Double.compare(total, 0.0) > 0) {
                recommendations.add(new SkillRecommendation(e.getKey(), total));
            }
        }
        return recommendations;
    }

    private void fillMapOfTotalImprovement(Map<String, Double> totalImprovement,
                                           Set<Skill> missingSkills, Candidate candidate,
                                           SimilarityStrategy strategy, double currentSimilarityScore,
                                           JobPosting jobPosting) {
        for (Skill skill : missingSkills) {
            Set<Skill> temp = new HashSet<>(candidate.getSkills());
            temp.add(skill);
            double newSimilarityScore = strategy.calculateSimilarity(
                temp, jobPosting.getRequiredSkills());

            double improvement = newSimilarityScore - currentSimilarityScore;
            SkillRecommendation recommendation = new SkillRecommendation(skill.name(), improvement);

            String name = skill.name();
            Double prev = totalImprovement.get(name);
            if (prev == null) {
                totalImprovement.put(name, improvement);
            } else {
                totalImprovement.put(name, prev + improvement);
            }
        }
    }
}
