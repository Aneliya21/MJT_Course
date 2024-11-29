package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AthleteTest {

    private Athlete athlete;

    @BeforeEach
    void setUp() {
        athlete = new Athlete("1", "100yo", "USA");
    }

    @Test
    void testEqualsNull() {
        assertNotEquals(athlete, null, "An athlete should not be equal to null");
    }

    @Test
    void testEqualsDifferentClass() {
        assertNotEquals(athlete, "Other Athlete", "An athlete should not be equal to an object of a different class");
    }

    @Test
    void testHashCodeUnequalObjects() {
        Athlete differentAthlete = new Athlete("2", "Vancho", "Canada");
        assertNotEquals(athlete.hashCode(), differentAthlete.hashCode(), "Unequal objects should have different hash codes");
    }

    @Test
    void testValidAthleteCreation() {
        assertEquals("1", athlete.getIdentifier(), "Identifier should match the one provided during creation");
        assertEquals("100yo", athlete.getName(), "Name should match the one provided during creation");
        assertEquals("USA", athlete.getNationality(), "Nationality should match the one provided during creation");
        assertTrue(athlete.getMedals().isEmpty(), "New athlete should have no medals");
    }

    @Test
    void testAddMedal() {
        Medal goldMedal = Medal.GOLD;
        athlete.addMedal(goldMedal);

        List<Medal> medals = new ArrayList<>(athlete.getMedals()); // Convert to List for ordering
        assertTrue(medals.contains(goldMedal), "The added medal should be in the athlete's medals list");
        assertEquals(1, medals.size(), "The athlete should have exactly one medal");
        assertEquals(Medal.GOLD, medals.get(0), "The first medal in the list should be GOLD");
    }

    @Test
    void testAddMultipleMedals() {
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.SILVER);

        List<Medal> medals = new ArrayList<>(athlete.getMedals());
        assertEquals(3, medals.size(), "The athlete should have 3 medals in total");
        assertEquals(Medal.GOLD, medals.get(0), "The first medal should be GOLD");
        assertEquals(Medal.GOLD, medals.get(1), "The second medal should also be GOLD");
        assertEquals(Medal.SILVER, medals.get(2), "The third medal should be SILVER");
    }

    @Test
    void testAddNullMedalThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> athlete.addMedal(null), "Adding a null medal should throw IllegalArgumentException");
    }

    @Test
    void testGetMedalsReturnsUnmodifiableList() {
        Medal goldMedal = Medal.GOLD;
        athlete.addMedal(goldMedal);

        List<Medal> medals = (List<Medal>) athlete.getMedals();

        assertThrows(UnsupportedOperationException.class, () -> medals.add(Medal.SILVER),
            "The medals list should be unmodifiable");
    }

    @Test
    void testEqualsSameAttributes() {
        Athlete anotherAthlete = new Athlete("1", "100yo", "USA");

        assertEquals(athlete, anotherAthlete, "Athletes with the same name, nationality, and medals should be equal");
    }

    @Test
    void testNotEqualsDifferentAttributes() {
        Athlete differentAthlete = new Athlete("5", "Vancho", "Canada");

        assertNotEquals(athlete, differentAthlete, "Athletes with different attributes should not be equal");
    }

    @Test
    void testHashCodeConsistency() {
        int hashCode1 = athlete.hashCode();
        int hashCode2 = athlete.hashCode();

        assertEquals(hashCode1, hashCode2, "Hash code should be consistent across multiple calls");
    }
}
