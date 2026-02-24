package bg.sofia.uni.fmi.mjt.fittrack.workout;

import java.util.Comparator;

public sealed interface Workout
    permits WorkoutAbstract {

    int MIN_DIFFICULTY = 1;
    int MAX_DIFFICULTY = 5;

    /**
     * Returns the name of the workout.
     *
     * @return the workout name.
     */
    String getName();

    /**
     * Returns the duration of the workout in minutes.
     *
     * @return the duration in minutes.
     */
    int getDuration();

    /**
     * Returns the number of calories burned by performing the workout.
     *
     * @return the calories burned.
     */
    int getCaloriesBurned();

    /**
     * Returns the difficulty of the workout (1 - easy, 5 - very hard).
     *
     * @return the difficulty.
     */
    int getDifficulty();

    /**
     * Returns the type of the workout.
     *
     * @return the workout type.
     */
    WorkoutType getType();

    Comparator<Workout> BY_CALORIES_DESC = new Comparator<Workout>() {
        @Override
        public int compare(Workout w1, Workout w2) {
            int cmp = Integer.compare(w2.getCaloriesBurned(), w1.getCaloriesBurned());
            if (cmp != 0) {
                return cmp;
            }

            cmp = Integer.compare(w2.getDifficulty(), w1.getDifficulty());
            if (cmp != 0) {
                return cmp;
            }

            return w1.getName().compareTo(w2.getName());
        }
    };

    Comparator<Workout> BY_DIFFICULTY_ASC = new Comparator<Workout>() {
        @Override
        public int compare(Workout w1, Workout w2) {
            int cmp = Integer.compare(w1.getDifficulty(), w2.getDifficulty());
            if (cmp != 0) {
                return cmp;
            }

            cmp = Integer.compare(w1.getCaloriesBurned(), w2.getCaloriesBurned());
            if (cmp != 0) {
                return cmp;
            }

            return w1.getName().compareTo(w2.getName());
        }
    };

}