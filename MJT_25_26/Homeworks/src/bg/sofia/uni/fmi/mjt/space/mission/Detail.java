package Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission;

public record Detail(String rocketName,
                     String payload) {
    public Detail {
        if (rocketName == null || rocketName.isBlank()) {
            throw new IllegalArgumentException("Detail rocketName cannot be null or blank");
        }
        if (payload == null || payload.isBlank()) {
            throw new IllegalArgumentException("Detail payload cannot be null or blank");
        }
    }
}