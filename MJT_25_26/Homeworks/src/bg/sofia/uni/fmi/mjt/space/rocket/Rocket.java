package Homeworks.src.bg.sofia.uni.fmi.mjt.space.rocket;

import java.util.Optional;

public record Rocket(String id,
                     String name,
                     Optional<String> wiki,
                     Optional<Double> height) {
    public Rocket {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Rocket id cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Rocket name cannot be null or blank");
        }
    }
}
