package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity;

public record Employer(
    String companyName,
    String email) {

    public Employer {
        validateInput(companyName, email);
    }

    private static void validateInput(String companyName, String email) {
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("CompanyName cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
    }
}
