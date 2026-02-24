package Homeworks.test.bg.sofia.uni.fmi.mjt.space.mission;

import Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission.Detail;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission.Mission;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MissionTest {

    private Mission mission;
    private Detail detail;

    @BeforeEach
    void setUp() {
        detail = new Detail("exampleRocketName", "examplePayload");
    }

    @Test
    void testMissionWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> mission = new Mission(
            null, "exampleCompany",
            "exampleLocation", LocalDate.now(),
            detail, RocketStatus.STATUS_ACTIVE,
            Optional.of(15000.0), MissionStatus.SUCCESS),
            "Should throw IllegalArgumentException when mission id is null");
    }

    @Test
    void testMissionWhenIdIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> mission = new Mission(
            "", "exampleCompany",
            "exampleLocation", LocalDate.now(),
            detail, RocketStatus.STATUS_ACTIVE,
            Optional.of(15000.0), MissionStatus.SUCCESS),
            "Should throw IllegalArgumentException when mission id is blank");
    }

    @Test
    void testMissionWhenCompanyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> mission = new Mission(
            "exampleId", null,
            "exampleLocation", LocalDate.now(),
            detail, RocketStatus.STATUS_ACTIVE,
            Optional.of(15000.0), MissionStatus.SUCCESS),
            "Should throw IllegalArgumentException when company is null");
    }

    @Test
    void testMissionWhenCompanyIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> mission = new Mission(
            "exampleId", "",
            "exampleLocation", LocalDate.now(),
            detail, RocketStatus.STATUS_ACTIVE,
            Optional.of(15000.0), MissionStatus.SUCCESS),
            "Should throw IllegalArgumentException when company is blank");
    }

    @Test
    void testMissionWhenDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> mission = new Mission(
            "exampleId", "exampleCompany",
            "exampleLocation", null,
            detail, RocketStatus.STATUS_ACTIVE,
            Optional.of(15000.0), MissionStatus.SUCCESS),
            "Should throw IllegalArgumentException when date is null");
    }

    @Test
    void testMissionWhenDetailIsNull() {
        assertThrows(IllegalArgumentException.class, () -> mission = new Mission(
            "exampleId", "exampleCompany",
            "exampleLocation", LocalDate.now(),
            null, RocketStatus.STATUS_ACTIVE,
            Optional.of(15000.0), MissionStatus.SUCCESS),
            "Should throw IllegalArgumentException when detail is null");
    }

    @Test
    void testMissionWhenRocketStatusIsNull() {
        assertThrows(IllegalArgumentException.class, () -> mission = new Mission(
            "exampleId", "exampleCompany",
            "exampleLocation", LocalDate.now(),
            detail, null,
            Optional.of(15000.0), MissionStatus.SUCCESS),
            "Should throw IllegalArgumentException when rocketStatus is null");
    }

    @Test
    void testMissionWhenMissionStatusIsNull() {
        assertThrows(IllegalArgumentException.class, () -> mission = new Mission(
            "exampleId", "exampleCompany",
            "exampleLocation", LocalDate.now(),
            detail, RocketStatus.STATUS_ACTIVE,
            Optional.of(15000.0), null),
            "Should throw IllegalArgumentException when missionStatus is null");
    }

    @Test
    void testValidMission() {
        mission = new Mission(
            "exampleId", "exampleCompany",
            "exampleLocation", LocalDate.now(),
            detail, RocketStatus.STATUS_ACTIVE,
            Optional.of(15000.0), MissionStatus.SUCCESS);

        assertEquals("exampleId", mission.id());
        assertEquals("exampleCompany", mission.company());
        assertEquals("exampleLocation", mission.location());
    }
}
