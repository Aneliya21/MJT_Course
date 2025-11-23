package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public class CaloriesWorkoutFilter extends BoundsFilter {

    public CaloriesWorkoutFilter(int min, int max) {
        super(min, max);
    }

    @Override
    protected int valueOf(Workout workout) {
        return workout.getCaloriesBurned();
    }
}
