package Homeworks.test.bg.sofia.uni.fmi.mjt.space.mission;

import org.junit.jupiter.api.Test;

import Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission.Detail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DetailTest {

    private Detail detail;

    @Test
    void testDetailWhenRocketNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> detail = new Detail(
            null, "examplePayload"
            ), "Should throw IllegalArgumentException when rocket name is null");
    }

    @Test
    void testDetailWhenRocketNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> detail = new Detail(
            "", "examplePayload"
            ), "Should throw IlegalArgumentException when rocket name is blank");
    }

    @Test
    void testDetailWhenPayloadIsNull() {
        assertThrows(IllegalArgumentException.class, () -> detail = new Detail(
            "exampleRocketName", null
            ), "Should throw IllegalArgumentException when payload is null");
    }

    @Test
    void testDetailWhenPayloadIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> detail = new Detail(
            "exampleRocketName", ""
            ), "Should throw IllegalArgumentException when payload is blank");
    }

    @Test
    void testValidDetail() {
        detail = new Detail("exampleRocketName", "examplePayload");

        assertEquals("exampleRocketName", detail.rocketName());
        assertEquals("examplePayload", detail.payload());
    }

}
