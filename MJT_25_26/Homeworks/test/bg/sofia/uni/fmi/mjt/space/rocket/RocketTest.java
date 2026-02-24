package Homeworks.test.bg.sofia.uni.fmi.mjt.space.rocket;

import org.junit.jupiter.api.Test;

import Homeworks.src.bg.sofia.uni.fmi.mjt.space.rocket.Rocket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RocketTest {

    private Rocket rocket;

    @Test
    void testRocketWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> rocket = new Rocket(null, "exampleName", null, null),
            "Should throw IllegalArgumentException when rocket id is null");
    }

    @Test
    void testRocketWhenIdIsBlank() {
        assertThrows(IllegalArgumentException.class,
            () -> rocket = new Rocket("", "exampleName", null, null),
            "Should throw IllegalArgumentException when rocket id is blank");
    }

    @Test
    void testRocketWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> rocket = new Rocket("exampleId", null, null, null),
            "Should throw IllegalArgumentException when rocket name is null");
    }

    @Test
    void testRocketWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
            () -> rocket = new Rocket("exampleId", "", null, null),
            "Should throw IllegalArgumentException when rocket name is blank");
    }

    @Test
    void testValidRocket() {
        rocket = new Rocket("exampleId", "exampleName", null, null);
        assertEquals("exampleId", rocket.id());
        assertEquals("exampleName", rocket.name());
    }
}
