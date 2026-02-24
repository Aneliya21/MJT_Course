package bg.sofia.uni.fmi.mjt.fittrack.workout;

public final class YogaSession extends WorkoutAbstract {

    public YogaSession(String name, int duration, int caloriesBurned, int difficulty) {
        super(name, duration, caloriesBurned, difficulty);
    }

    @Override
    public WorkoutType getType() {
        return WorkoutType.YOGA;
    }
}
