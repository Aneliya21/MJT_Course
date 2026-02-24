package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.WorkoutType;

public class TypeWorkoutFilter implements WorkoutFilter {

    private WorkoutType type;

    public TypeWorkoutFilter(WorkoutType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        this.type = type;
    }

    @Override
    public boolean matches(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null");
        }

        return type.equals(workout.getType());
    }
}
