package bg.sofia.uni.fmi.mjt.fittrack.workout;

import bg.sofia.uni.fmi.mjt.fittrack.exception.InvalidWorkoutException;

public abstract non-sealed class WorkoutAbstract implements Workout {

    private String name;
    private int duration;
    private int caloriesBurned;
    private int difficulty;

    public WorkoutAbstract(String name, int duration, int caloriesBurned, int difficulty) {
        validateName(name);
        this.name = name;

        validateDuration(duration);
        this.duration = duration;

        validateCaloriesBurned(caloriesBurned);
        this.caloriesBurned = caloriesBurned;

        validateDifficulty(difficulty);
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return String.format("%s[name=%s, duration=%d, caloriesBurned=%d, difficulty=%d]",
            this.getClass().getSimpleName(),
            name,
            duration,
            caloriesBurned,
            difficulty);
    }

    @Override
    public String getName() {
        validateName(name);
        return name;
    }

    @Override
    public int getDuration() {
        validateDuration(duration);
        return duration;
    }

    @Override
    public int getCaloriesBurned() {
        validateCaloriesBurned(caloriesBurned);
        return caloriesBurned;
    }

    @Override
    public int getDifficulty() {
        validateDifficulty(difficulty);
        return difficulty;
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new InvalidWorkoutException("Name of yoga workout cannot be null or empty");
        }
    }

    private void validateDuration(int duration) {
        if (duration <= 0) {
            throw new InvalidWorkoutException("Duration of yoga workout cannot be negative value");
        }
    }

    private void validateCaloriesBurned(int caloriesBurned) {
        if (caloriesBurned <= 0) {
            throw new InvalidWorkoutException("CaloriesBurned of yoga workout cannot be negative value");
        }
    }

    private void validateDifficulty(int difficulty) {
        if (difficulty < MIN_DIFFICULTY || difficulty > MAX_DIFFICULTY) {
            throw new InvalidWorkoutException("Difficulty of yoga workout cannot be out of difficulty bounds");
        }
    }
}
