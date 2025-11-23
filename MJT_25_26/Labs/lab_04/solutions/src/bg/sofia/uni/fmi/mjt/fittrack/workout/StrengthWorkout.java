package bg.sofia.uni.fmi.mjt.fittrack.workout;

public final class StrengthWorkout extends WorkoutAbstract {

    public StrengthWorkout(String name, int duration, int caloriesBurned, int difficulty) {
        super(name, duration, caloriesBurned, difficulty);
    }

    @Override
    public WorkoutType getType() {
        return WorkoutType.STRENGTH;
    }
}
