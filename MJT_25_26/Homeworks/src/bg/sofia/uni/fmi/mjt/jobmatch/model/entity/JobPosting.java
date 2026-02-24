package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity;

import java.util.Set;

public class JobPosting implements Comparable<JobPosting> {

    private String id;
    private String title;
    private String employerEmail;
    private Set<Skill> requiredSkills;
    private Education requiredEducation;
    private int requiredYearsOfExperience;
    private double salary;

    public JobPosting(String id, String title, String employerEmail, Set<Skill> requiredSkills,
                      Education requiredEducation, int requiredYearsOfExperience, double salary) {
        validateInput(id, title, employerEmail, requiredSkills, requiredEducation, requiredYearsOfExperience, salary);
        this.id = id;
        this.title = title;
        this.employerEmail = employerEmail;
        this.requiredSkills = requiredSkills;
        this.requiredEducation = requiredEducation;
        this.requiredYearsOfExperience = requiredYearsOfExperience;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getEmployerEmail() {
        return employerEmail;
    }

    public int getRequiredYearsOfExperience() {
        return requiredYearsOfExperience;
    }

    public Set<Skill> getRequiredSkills() {
        return requiredSkills;
    }

    public Education getRequiredEducation() {
        return requiredEducation;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public int compareTo(JobPosting other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare with null");
        }

        int cmp = Double.compare(other.salary, this.salary);
        if (cmp != 0) {
            return cmp;
        }

        return this.title.compareTo(other.title);
    }

    private static void validateInput(String id, String title, String employerEmail, Set<Skill> requiredSkills,
                               Education requiredEducation, int requiredYearsOfExperience, double salary) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id cannot be null or blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (employerEmail == null || employerEmail.isBlank()) {
            throw new IllegalArgumentException("EmployerEmail cannot be null or blank");
        }
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            throw new IllegalArgumentException("RequiredSkills cannot be null or empty");
        }
        if (requiredEducation == null) {
            throw new IllegalArgumentException("RequiredEducation cannot be null");
        }
        if (requiredYearsOfExperience < 0) {
            throw new IllegalArgumentException("RequiredYearsOfExperience cannot be negative");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
    }
}
