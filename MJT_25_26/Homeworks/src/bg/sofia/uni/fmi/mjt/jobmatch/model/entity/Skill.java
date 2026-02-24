package Homeworks.bg.sofia.uni.fmi.mjt.jobmatch.model.entity;

public record Skill(
    String name,
    int level) implements Comparable<Skill> {

    private static final int MIN_LEVEL = 0;
    private static final int MAX_LEVEL = 5;

    public Skill {
        validateInput(name, level);
    }

    @Override
    public int compareTo(Skill other) {
        int result = this.name.compareTo(other.name);
        if (result != 0) {
            return result;
        }

        return Integer.compare(this.level, other.level);
    }

    private static void validateInput(String name, int level) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (level < MIN_LEVEL || level > MAX_LEVEL) {
            throw new IllegalArgumentException("Level cannot be less than: " + MIN_LEVEL +
                " and more than: " + MAX_LEVEL);
        }
    }
}
