package Homeworks.test.bg.sofia.uni.fmi.mjt.space.load;

import Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission.Detail;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission.Mission;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MissionLoaderTest {

    private MissionLoader loader;

    @BeforeEach
    void setUp() {
        loader = new MissionLoader();
    }

    @Test
    void testLoad_whenValidCSV_thenReturnCorrectMissions() throws IOException {
        String csv = "id,company,location,date,detail,rocketStatus,cost,missionStatus\n" +
            "M1,NASA,KSC FL USA,Sun Jan 01, 2023,Falcon 9|Starlink,ACTIVE,1000,SUCCESS\n" +
            "M2,SpaceX,Vandenberg CA USA,Mon Feb 01, 2023,Falcon Heavy,ACTIVE,500,FAILURE";

        List<Mission> missions = loader.load(new StringReader(csv));

        assertEquals(2, missions.size());

        Mission first = missions.get(0);
        assertEquals("M1", first.id());
        assertEquals("NASA", first.company());
        assertEquals("Kennedy Space Center, FL, USA", first.location());
        assertEquals(LocalDate.of(2023, 1, 1), first.date());
        assertEquals(new Detail("Falcon 9", "Starlink"), first.detail());
        assertEquals(RocketStatus.STATUS_ACTIVE, first.rocketStatus());
        assertEquals(Optional.of(1000.0), first.cost());
        assertEquals(MissionStatus.SUCCESS, first.missionStatus());

        Mission second = missions.get(1);
        assertEquals("M2", second.id());
        assertEquals(new Detail("Falcon Heavy", ""), second.detail());
        assertEquals(MissionStatus.FAILURE, second.missionStatus());
    }

    @Test
    void testLoad_whenEmptyCSV_thenReturnEmptyList() throws IOException {
        String csv = "id,company,location,date,detail,rocketStatus,cost,missionStatus\n";
        List<Mission> missions = loader.load(new StringReader(csv));

        assertTrue(missions.isEmpty());
    }

    @Test
    void testLoad_whenInvalidLine_thenSkipLine() throws IOException {
        String csv = "id,company,location,date,detail,rocketStatus,cost,missionStatus\n" +
            "INVALID LINE\n" +
            "M1,NASA,\"Kennedy Space Center, FL, USA\",\"Sun Jan 01, 2023\",\"Falcon 9|Starlink\",ACTIVE,1000,SUCCESS";

        List<Mission> missions = loader.load(new StringReader(csv));

        assertEquals(1, missions.size());
        assertEquals("M1", missions.get(0).id());
    }

    @Test
    void testLoad_whenMissingCost_thenReturnEmptyOptional() throws IOException {
        String csv = "id,company,location,date,detail,rocketStatus,cost,missionStatus\n" +
            "M1,NASA,\"Kennedy Space Center, FL, USA\",\"Sun Jan 01, 2023\",\"Falcon 9|Starlink\",ACTIVE,,SUCCESS";

        List<Mission> missions = loader.load(new StringReader(csv));

        assertEquals(1, missions.size());
        assertEquals(Optional.empty(), missions.get(0).cost());
    }

    @Test
    void testLoad_whenInvalidRocketStatus_thenThrowException() {
        String csv = "id,company,location,date,detail,rocketStatus,cost,missionStatus\n" +
            "M1,NASA,\"Kennedy Space Center, FL, USA\",\"Sun Jan 01, 2023\",\"Falcon 9|Starlink\",INVALID,1000,SUCCESS";

        assertThrows(IllegalArgumentException.class, () -> loader.load(new StringReader(csv)));
    }

    @Test
    void testLoad_whenInvalidMissionStatus_thenThrowException() {
        String csv = "id,company,location,date,detail,rocketStatus,cost,missionStatus\n" +
            "M1,NASA,\"Kennedy Space Center, FL, USA\",\"Sun Jan 01, 2023\",\"Falcon 9|Starlink\",ACTIVE,1000,INVALID";

        assertThrows(IllegalArgumentException.class, () -> loader.load(new StringReader(csv)));
    }
}
