package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public class NameWorkoutFilter implements WorkoutFilter {

    private String keyword;
    private boolean caseSensitive;

    public NameWorkoutFilter(String keyword, boolean caseSensitive) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }

        this.keyword = keyword;
        this.caseSensitive = caseSensitive;
    }

    @Override
    public boolean matches(Workout workout) {
        if (workout == null || workout.getName() == null) {
            return false;
        }

        if (caseSensitive) {
            return workout.getName().contains(keyword);
        } else {
            return workout.getName().toLowerCase().contains(keyword.toLowerCase());
        }
    }
}
