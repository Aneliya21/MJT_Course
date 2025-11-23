package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public abstract class BoundsFilter implements WorkoutFilter {
    private int min;
    private int max;

    BoundsFilter(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot be more than max");
        }
        if (min < 0) {
            throw new IllegalArgumentException("Min cannot be negative value");
        }
        if (max < 0) {
            throw new IllegalArgumentException("Max cannot be negative value");
        }

        this.min = min;
        this.max = max;
    }

    protected abstract int valueOf(Workout workout);

    @Override
    public boolean matches(Workout workout) {
        if (workout == null) {
            return false;
        }

        int value = valueOf(workout);
        return min <= value && value <= max;
    }
}
