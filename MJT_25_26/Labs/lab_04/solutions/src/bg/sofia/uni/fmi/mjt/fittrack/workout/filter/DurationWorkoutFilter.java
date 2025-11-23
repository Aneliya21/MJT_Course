package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public class DurationWorkoutFilter extends  BoundsFilter {

    public DurationWorkoutFilter(int min, int max) {
        super(min, max);
    }

    @Override
    protected int valueOf(Workout workout) {
        return workout.getDuration();
    }
}
