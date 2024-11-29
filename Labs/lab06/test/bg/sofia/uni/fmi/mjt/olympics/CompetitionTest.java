package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompetitionTest {

    private Competitor competitor1;
    private Competitor competitor2;

    @BeforeEach
    void setUp() {
        competitor1 = mock(Competitor.class);
        competitor2 = mock(Competitor.class);

        when(competitor1.toString()).thenReturn("Competitor1");
        when(competitor2.toString()).thenReturn("Competitor2");
    }

    @Test
    void testEqualsNull() {
        Competition competition = new Competition("Olympics", "Running", Set.of(competitor1));
        assertNotEquals(competition, null, "A competition should not be equal to null");
    }

    @Test
    void testEqualsDifferentClass() {
        Competition competition = new Competition("Olympics", "Running", Set.of(competitor1));
        assertNotEquals(competition, "Some String", "A competition should not be equal to an object of a different class");
    }

    @Test
    void testEqualsDifferentDisciplines() {
        Competition competition1 = new Competition("Olympics", "Running", Set.of(competitor1));
        Competition competition2 = new Competition("Olympics", "Swimming", Set.of(competitor1));
        assertNotEquals(competition1, competition2, "Competitions with different disciplines should not be equal");
    }

    @Test
    void testHashCodeUnequalObjects() {
        Competition competition1 = new Competition("Olympics", "Running", Set.of(competitor1));
        Competition competition2 = new Competition("Olympics", "Swimming", Set.of(competitor1));
        assertNotEquals(competition1.hashCode(), competition2.hashCode(), "Competitions with different disciplines should have different hash codes");
    }

    @Test
    void testValidCompetitionCreation() {
        Set<Competitor> competitors = Set.of(competitor1, competitor2);

        Competition competition = new Competition("Olympics", "Running", competitors);

        assertEquals("Olympics", competition.name(), "Name should match the one provided during creation");
        assertEquals("Running", competition.discipline(), "Discipline should match the one provided during creation");
        assertEquals(competitors, competition.competitors(), "Competitors set should match the one provided during creation");
    }

    @Test
    void testUnmodifiableCompetitorsSet() {
        Set<Competitor> competitors = Set.of(competitor1);

        Competition competition = new Competition("Olympics", "Swimming", competitors);

        assertThrows(UnsupportedOperationException.class, () -> competition.competitors().add(mock(Competitor.class)),
            "The competitors set should be unmodifiable");
    }

    @Test
    void testCompetitionEquality() {
        Set<Competitor> competitors1 = Set.of(competitor1, competitor2);
        Set<Competitor> competitors2 = Set.of(competitor1); // Different competitors set, but same name and discipline

        Competition competition1 = new Competition("Olympics", "Cycling", competitors1);
        Competition competition2 = new Competition("Olympics", "Cycling", competitors2);

        assertEquals(competition1, competition2, "Competitions with the same name and discipline should be equal");
        assertEquals(competition1.hashCode(), competition2.hashCode(), "Equal objects should have the same hash code");
    }

    @Test
    void testCompetitionInequality() {
        Set<Competitor> competitors = Set.of(competitor1);

        Competition competition1 = new Competition("Olympics", "Cycling", competitors);
        Competition competition2 = new Competition("World Championship", "Cycling", competitors);

        assertNotEquals(competition1, competition2, "Competitions with different names should not be equal");
    }

    @Test
    void testNullNameThrowsException() {
        Set<Competitor> competitors = Set.of(competitor1);

        assertThrows(IllegalArgumentException.class, () -> new Competition(null, "Running", competitors),
            "Creating a competition with a null name should throw IllegalArgumentException");
    }

    @Test
    void testBlankNameThrowsException() {
        Set<Competitor> competitors = Set.of(competitor1);

        assertThrows(IllegalArgumentException.class, () -> new Competition(" ", "Swimming", competitors),
            "Creating a competition with a blank name shuold throw IlleagalArgumentException");
    }

    @Test
    void testNullDisciplineThrowsException() {
        Set<Competitor> competitors = Set.of(competitor1);

        assertThrows(IllegalArgumentException.class, () -> new Competition("Olympics", null, competitors),
            "Creating a competition with a null discipline should throw IllegalArgumentException");
    }

    @Test
    void testBlankDisciplineThrowsException() {
        Set<Competitor> competitors = Set.of(competitor1);

        assertThrows(IllegalArgumentException.class, () -> new Competition("Olympics", " ", competitors),
            ";Creating a competition with a blank discipline should throw IllegalArgumentException");
    }

    @Test
    void testNullCompetitorsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("Olympics", "Running", null),
            "Creating a competition with null competitors should throw IllegalArgumentException");
    }

    @Test
    void testEmptyCompetitorsThrowsException() {
        Set<Competitor> competitors = Set.of(competitor1);

        assertThrows(IllegalArgumentException.class, () -> new Competition("Olympics", "Running", Set.of()),
            "Creating a competition with an empty competitors should throw IllegalArgumentException");
    }
}
