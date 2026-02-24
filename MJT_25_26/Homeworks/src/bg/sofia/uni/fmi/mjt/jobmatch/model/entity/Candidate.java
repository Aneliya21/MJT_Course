package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity;

import java.util.Set;

public class Candidate {

    private String name;
    private String email;
    private Set<Skill> skills;
    private Education education;
    private int yearsOfExperience;

    public Candidate(String name, String email, Set<Skill> skills, Education education, int yearsOfExperience) {
        validateInputData(name, email, skills, yearsOfExperience);
        this.name = name;
        this.email = email;
        this.skills = skills;
        this.education = education;
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return this.email;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public Education getEducation() {
        return education;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    private static void validateInputData(String name, String email, Set<Skill> skills, int yearsOfExperience) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (skills == null || skills.isEmpty()) {
            throw new IllegalArgumentException("Skills cannot be null or empty");
        }
        if (yearsOfExperience < 0) {
            throw new IllegalArgumentException("YearsOfExperience cannot be negative");
        }
    }
}
