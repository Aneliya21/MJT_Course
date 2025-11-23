package bg.sofia.uni.fmi.mjt.fittrack;

import bg.sofia.uni.fmi.mjt.fittrack.exception.OptimalPlanImpossibleException;
import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.WorkoutType;
import bg.sofia.uni.fmi.mjt.fittrack.workout.filter.WorkoutFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class FitPlanner implements FitPlannerAPI {

    private final Collection<Workout> availableWorkouts;

    public FitPlanner(Collection<Workout> availableWorkouts) {
        validateAvailableWorkouts(availableWorkouts);
        this.availableWorkouts = availableWorkouts;
    }

    @Override
    public List<Workout> findWorkoutsByFilters(List<WorkoutFilter> filters) {
        validateWorkoutFilters(filters);

        if (filters.isEmpty()) {
            return new ArrayList<>(availableWorkouts);
        }

        List<Workout> result = new ArrayList<>();
        for (Workout workout : availableWorkouts) {

            boolean allMatch = true;
            for (WorkoutFilter filter : filters) {

                if (!filter.matches(workout)) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                result.add(workout);
            }
        }
        return result;
    }

    @Override
    public List<Workout> generateOptimalWeeklyPlan(int totalMinutes) throws OptimalPlanImpossibleException {
        validateTotalMinutes(totalMinutes);

        if (totalMinutes == 0) {
            return Collections.emptyList();
        }

        List<Workout> items = new ArrayList<>(availableWorkouts);

        boolean anyFit = findAnyFit(totalMinutes);

        if (!anyFit) {
            throw new OptimalPlanImpossibleException("No workout fits the given time limit");
        }

        int[][] dp = buildKnapsackTable(items, totalMinutes);
        List<Workout> selected = reconstructWorkouts(dp, items, totalMinutes);

        if (selected.isEmpty()) {
            throw new OptimalPlanImpossibleException("No workout fits the given time limit");
        }

        selected.sort(Workout.BY_CALORIES_DESC);
        return Collections.unmodifiableList(selected);
    }

    @Override
    public Map<WorkoutType, List<Workout>> getWorkoutsGroupedByType() {
        Map<WorkoutType, List<Workout>> result = new HashMap<>();

        for (Workout workout : availableWorkouts) {
            result.computeIfAbsent(workout.getType(), k -> new ArrayList<>())
                .add(workout);
        }

        for (Map.Entry<WorkoutType, List<Workout>> e : result.entrySet()) {
            e.setValue(Collections.unmodifiableList(e.getValue()));
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public List<Workout> getWorkoutsSortedByCalories() {
        List<Workout> copy = new ArrayList<>(availableWorkouts);
        Collections.sort(copy, Workout.BY_CALORIES_DESC);
        return Collections.unmodifiableList(copy);
    }

    @Override
    public List<Workout> getWorkoutsSortedByDifficulty() {
        List<Workout> copy = new ArrayList<>(availableWorkouts);
        Collections.sort(copy, Workout.BY_DIFFICULTY_ASC);
        return Collections.unmodifiableList(copy);
    }

    @Override
    public Set<Workout> getUnmodifiableWorkoutSet() {
        return Set.copyOf(availableWorkouts);
    }

    private void validateAvailableWorkouts(Collection<Workout> availableWorkouts) {
        if (availableWorkouts == null) {
            throw new IllegalArgumentException("AvailableWorkouts cannot be null");
        }
    }

    private void validateWorkoutFilters(List<WorkoutFilter> filters) {
        if (filters == null) {
            throw new IllegalArgumentException("Filters cannot be null");
        }
    }

    private void validateTotalMinutes(int totalMinutes) {
        if (totalMinutes < 0) {
            throw new IllegalArgumentException("TotalMinutes cannot be negative value");
        }
    }

    private boolean findAnyFit(int totalMinutes) {
        List<Workout> items = new ArrayList<>(availableWorkouts);

        for (Workout workout : items) {
            if (workout.getDuration() <= totalMinutes) {
                return true;
            }
        }

        return false;
    }

    private int[][] buildKnapsackTable(List<Workout> items, int totalMinutes) {
        int size = items.size();
        int[][] dp = new int[size + 1][totalMinutes + 1];

        for (int i = 1; i <= size; i++) {
            Workout currentWorkout = items.get(i - 1);
            int workoutDuration = currentWorkout.getDuration();
            int workoutCalories = currentWorkout.getCaloriesBurned();

            for (int w = 0; w <= totalMinutes; w++) {
                if (workoutDuration <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - workoutDuration] + workoutCalories);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        return dp;
    }

    private List<Workout> reconstructWorkouts(int[][] dp, List<Workout> items, int totalMinutes) {
        int w = totalMinutes;
        int size = items.size();
        List<Workout> selected = new ArrayList<>();

        for (int i = size; i >= 1; i--) {
            if (w < 0) {
                break;
            }

            if (dp[i][w] != dp[i - 1][w]) {
                Workout chosen = items.get(i - 1);
                selected.add(chosen);
                w -= chosen.getDuration();
            }
        }

        return selected;
    }
}
