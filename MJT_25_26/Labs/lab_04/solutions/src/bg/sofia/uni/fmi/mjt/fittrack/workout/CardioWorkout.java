package bg.sofia.uni.fmi.mjt.fittrack.workout;

public final class CardioWorkout extends WorkoutAbstract {

    public CardioWorkout(String name, int duration, int caloriesBurned, int difficulty) {
        super(name, duration, caloriesBurned, difficulty);
    }

    @Override
    public WorkoutType getType() {
        return WorkoutType.CARDIO;
    }

}
