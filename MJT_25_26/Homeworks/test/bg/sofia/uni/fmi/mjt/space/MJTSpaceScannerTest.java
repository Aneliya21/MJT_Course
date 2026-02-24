package Homeworks.test.bg.sofia.uni.fmi.mjt.space;

import Homeworks.src.bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission.Detail;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission.Mission;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MJTSpaceScannerTest {

    private SecretKey secretKey;
    private MJTSpaceScanner scanner;
    private Mission mission1;
    private Mission mission2;
    private Rocket rocket1;

    @BeforeEach
    void setUp() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        secretKey = keyGen.generateKey();

        Reader dummyMissionsReader = new StringReader("");
        Reader dummyRocketsReader = new StringReader("");

        scanner = new MJTSpaceScanner(dummyMissionsReader, dummyRocketsReader, secretKey);

        Detail detail1 = new Detail("Falcon 9", "payload");
        Detail detail2 = new Detail("Falcon Heavy", "payload");

        mission1 = new Mission(
            "M1",
            "NASA",
            "Kennedy Space Center, FL, USA",
            LocalDate.of(2023, 1, 1),
            detail1,
            RocketStatus.STATUS_ACTIVE,
            Optional.of(1000.0),
            MissionStatus.SUCCESS
        );

        mission2 = new Mission(
            "M2",
            "SpaceX",
            "Vandenberg AFB, CA, USA",
            LocalDate.of(2023, 2, 1),
            detail2,
            RocketStatus.STATUS_ACTIVE,
            Optional.of(500.0),
            MissionStatus.FAILURE
        );

        rocket1 = new Rocket(
            "id1",
            "Falcon 9",
            Optional.of("https://en.wikipedia.org/wiki/Falcon_9"),
            Optional.of(70.0)
        );

        var missionsField = MJTSpaceScanner.class.getDeclaredField("missions");
        missionsField.setAccessible(true);
        missionsField.set(scanner, new ArrayList<>(List.of(mission1, mission2)));

        var rocketsField = MJTSpaceScanner.class.getDeclaredField("rockets");
        rocketsField.setAccessible(true);
        rocketsField.set(scanner, new ArrayList<>(List.of(rocket1)));
    }

    @Test
    void testGetAllMissionsWhenCalledThenReturnsAllMissions() {
        assertEquals(2, scanner.getAllMissions().size());
    }

    @Test
    void testGetAllMissionsWhenStatusIsSuccessThenReturnsOnlySuccessMissions() {
        assertEquals(1, scanner.getAllMissions(MissionStatus.SUCCESS).size());
    }

    @Test
    void testGetMostDesiredLocationForMissionsPerCompanyWhenCalledThenReturnsTopLocation() {
        Map<String, String> result = scanner.getMostDesiredLocationForMissionsPerCompany();
        assertEquals("Kennedy Space Center, FL, USA", result.get("NASA"));
    }

    @Test
    void testGetWikiPageForRocketWhenCalledThenReturnsOptional() {
        Map<String, Optional<String>> wikiMap = scanner.getWikiPageForRocket();
        assertTrue(wikiMap.get("Falcon 9").isPresent());
    }

    @Test
    void testSaveMostReliableRocketWhenCalledThenWritesToOutput() throws CipherException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scanner.saveMostReliableRocket(baos, LocalDate.of(2023,1,1), LocalDate.of(2023,12,31));
        assertTrue(baos.size() > 0);
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWhenTimeFrameInvalidThenThrows() {
        assertThrows(TimeFrameMismatchException.class, () ->
            scanner.getLocationWithMostSuccessfulMissionsPerCompany(
                LocalDate.of(2023,12,31), LocalDate.of(2023,1,1)));
    }
}
