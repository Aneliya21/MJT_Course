package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.comparator.NationMedalComparator;
import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import static java.util.Collections.reverse;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MJTOlympicsTest {

    @Mock
    public CompetitionResultFetcher competitionResultFetcher;
    public Set<Competitor> competitors;
    public MJTOlympics olympics;
    public TreeSet<Competitor> sorted;
    public Competitor[] competitorsArr;

    void runUpdateMedalStats(Competition competition, Competitor[] competitors) {

        when(competitionResultFetcher.getResult(competition)).thenReturn(sorted);
        when(sorted.size()).thenReturn(this.competitors.size());
        when(sorted.pollFirst()).thenReturn(competitors[0]).thenReturn(competitors[1]).thenReturn(competitors[2]);

        olympics.updateMedalStatistics(competition);
    }

    @BeforeEach
    void setUp() {
        competitors = new HashSet<>();
        competitors.add(new Athlete("1", "100yo", "Bulgaria"));
        competitors.add(new Athlete("2", "Pancho", "Bulgaria"));
        competitors.add(new Athlete("3", "Jhon", "USA"));

        olympics = new MJTOlympics(competitors, competitionResultFetcher);

        sorted = mock(TreeSet.class);
        competitorsArr = competitors.toArray(new Competitor[0]);
    }

    @Test
    @Order(1)
    void testCompetitionValidation() {
        assertThrows(IllegalArgumentException.class, () -> olympics.updateMedalStatistics(null),
            "Competition cannot be null");

        Set<Competitor> comps1 = new HashSet<>(competitors);
        comps1.add(new Athlete("6", "Petar", "Netherlands"));
        Competition c = new Competition("Swimming", "200m/Butterfly", comps1);

        assertThrows(IllegalArgumentException.class, () -> olympics.updateMedalStatistics(c),
            "Competitors that are not registered for the competition cannot take part in it!");
    }

    @Test
    @Order(2)
    void testUpdateMedalTable() {
        runUpdateMedalStats(new Competition("Swimming", "100m/Freestyle", competitors), competitorsArr);

        assertEquals((competitorsArr[0]).getMedals().toArray()[0], Medal.GOLD,
            "Competitor who takes first place, should have Gold medal");
        assertEquals((competitorsArr[1]).getMedals().toArray()[0], Medal.SILVER,
            "Competitor who takes second place, should have Silver medal");
        assertEquals((competitorsArr[2]).getMedals().toArray()[0], Medal.BRONZE,
            "Competitor who takes third place, should have Bronze medal");

        assertEquals(olympics.getNationsMedalTable().get("Bulgaria").get(Medal.GOLD), 1,
            "Bulgaria should have gold medal");
        assertEquals(olympics.getNationsMedalTable().get("Bulgaria").get(Medal.SILVER), 1,
            "Bulgaria should have silver medal");
        assertEquals(olympics.getNationsMedalTable().get("USA").get(Medal.BRONZE), 1,
            "USA should have bronze medal");

        reverse(Arrays.asList(competitorsArr));
        runUpdateMedalStats(new Competition("Swimming", "200m/Freestyle", competitors), competitorsArr);

        assertEquals((competitorsArr[0]).getMedals().toArray()[1], Medal.GOLD,
            "Competitor who takes first place, should have Gold medal");
        assertEquals((competitorsArr[1]).getMedals().toArray()[1], Medal.SILVER,
            "Competitor who takes second place, should have Silver medal");
        assertEquals((competitorsArr[2]).getMedals().toArray()[1], Medal.BRONZE,
            "Competitor who takes third place, should have Bronze medal");

        assertEquals(olympics.getNationsMedalTable().get("Bulgaria").get(Medal.GOLD), 1,
            "Bulgaria should have 1 gold medal");
        assertEquals(olympics.getNationsMedalTable().get("Bulgaria").get(Medal.SILVER), 2,
            "Bulgaria should have 2 silver medals");
        assertEquals(olympics.getNationsMedalTable().get("USA").get(Medal.BRONZE), 1,
            "USA should have 1 bronze medal");
        assertEquals(olympics.getNationsMedalTable().get("Bulgaria").get(Medal.BRONZE), 1,
            "Bulgaria should have 1 bronze medal");
        assertEquals(olympics.getNationsMedalTable().get("USA").get(Medal.GOLD), 1,
            "USA should have 1 gold medal");
    }

    @Test
    @Order(3)
    void testNationValidation() {
        NationMedalComparator nationMedalComp = new NationMedalComparator(olympics);
        runUpdateMedalStats(new Competition("Swimming", "100m/Freestyle", competitors), competitorsArr);

        assertThrows(IllegalArgumentException.class, () -> nationMedalComp.compare(null, null),
            "Both arguments cannot be null");
        assertThrows(IllegalArgumentException.class, () -> nationMedalComp.compare(null, "Bulgaria"),
            "Either argument cannot be null");
        assertThrows(IllegalArgumentException.class, () -> nationMedalComp.compare("Scotland", "Bulgaria"),
            "Nations should be both registered to the olympics");

        assertDoesNotThrow(() -> nationMedalComp.compare("USA", "Bulgaria"),
            "No exception should be thrown");
    }

    @Test
    @Order(4)
    void testGetTotalMedals() {
        runUpdateMedalStats(new Competition("Swimming", "100m/Freestyle", competitors), competitorsArr);
        reverse(Arrays.asList(competitorsArr));
        runUpdateMedalStats(new Competition("Swimming", "200m/Freestyle", competitors), competitorsArr);

        assertEquals(4, olympics.getTotalMedals("Bulgaria"), "Bulgaria should have 4 medals");
        assertEquals(2, olympics.getTotalMedals("USA"), "USA should have 2 medals");
    }

    @Test
    @Order(5)
    void testMedalsComparator() {
        NationMedalComparator nationMedalComp = new NationMedalComparator(olympics);
        runUpdateMedalStats(new Competition("Swimming", "100m/Freestyle", competitors), competitorsArr);

        assertTrue(nationMedalComp.compare("USA", "Bulgaria") > 0, "Bulgaria should have more medals");

        competitors.remove(competitorsArr[0]);
        competitors.add(new Athlete("9", "Gabi", "France"));
        olympics = new MJTOlympics(competitors, competitionResultFetcher);
        runUpdateMedalStats(new Competition("Swimming", "200m/Freestyle", competitors), competitors.toArray(new Competitor[0]));
        nationMedalComp = new NationMedalComparator(olympics);

        assertTrue(nationMedalComp.compare("USA", "Bulgaria") > 0, "Bulgaria should have more medals that USA");
    }

    @Test
    @Order(6)
    void testNationsRankListNotEqualMedals() {
        runUpdateMedalStats(new Competition("Swimming", "100m/Freestyle", competitors), competitorsArr);
        reverse(Arrays.asList(competitorsArr));
        runUpdateMedalStats(new Competition("Swimming", "200m/Freestyle", competitors), competitorsArr);
        TreeSet<String> nations = olympics.getNationsRankList();

        assertEquals(nations.pollFirst(), "Bulgaria",
            "Bulgaria is supposed to be first due to having more medals");
        assertEquals(nations.pollFirst(), "USA",
            "USE is supposed to be second due to having less medals that Bulgaria");

        competitors.clear();
        competitors.add(new Athlete("9", "Emo", "Netherlands"));
        competitors.add(new Athlete("11", "100yo", "Bulgaria"));
        competitors.add(new Athlete("12", "Pancho", "Australia"));
        olympics = new MJTOlympics(competitors, competitionResultFetcher);
        competitorsArr = competitors.toArray(new Competitor[0]);
        runUpdateMedalStats(new Competition("Swimming", "100m/Freestyle", competitors), competitorsArr);
        reverse(Arrays.asList(competitorsArr));
        runUpdateMedalStats(new Competition("Swimming", "100m/Freestyle", competitors), competitorsArr);
        nations = olympics.getNationsRankList();

        assertEquals(nations.pollFirst(), "Australia", "Alphabetical order requires Australia to be first");
        assertEquals(nations.pollFirst(), "Bulgaria", "Alphabetical order requires Bulgaria to be second");
        assertEquals(nations.pollFirst(), "Netherlands", "Alphabetical order requires Netherlands to be third");
    }
}
