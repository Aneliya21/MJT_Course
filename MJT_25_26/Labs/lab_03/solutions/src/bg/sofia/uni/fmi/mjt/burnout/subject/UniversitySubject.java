package bg.sofia.uni.fmi.mjt.burnout.subject;

/**
 * @param name the name of the subject
 * @param credits number of credit hours for this subject
 * @param rating difficulty rating of the subject (1-5)
 * @param category the academic category this subject belongs to
 * @param neededStudyTime estimated study time in days required for this subject
 *
 * @throws IllegalArgumentException if the name of the subject is null or blank
 * @throws IllegalArgumentException if the credits are not positive
 * @throws IllegalArgumentException if the rating is not in its bounds
 * @throws IllegalArgumentException if the Category is null
 * @throws IllegalArgumentException if the neededStudy time is not positive
 */
public record UniversitySubject(String name, int credits, int rating, Category category, int neededStudyTime) {

    private static final int MIN_RATING_VALUE = 1;
    private static final int MAX_RATING_VALUE = 5;

    public UniversitySubject {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if (credits <= 0) {
            throw new IllegalArgumentException("Credits cannot be negative");
        }

        if (rating < MIN_RATING_VALUE || rating > MAX_RATING_VALUE) {
            throw new IllegalArgumentException("Rating should be value between 1 and 5");
        }

        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (neededStudyTime <= 0) {
            throw new IllegalArgumentException("Needed study time cannot be negative");
        }
    }
}