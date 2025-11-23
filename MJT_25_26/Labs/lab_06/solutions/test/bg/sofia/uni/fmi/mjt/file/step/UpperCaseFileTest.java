package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpperCaseFileTest {

    private UpperCaseFile upperCaseFile;

    @BeforeEach
    void setUp() {
        upperCaseFile = new UpperCaseFile();
    }

    @Test
    void testProcessWhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> upperCaseFile.process(null), "IlleagalArgumentException should be thrown");
    }

    @Test
    void testProcessWhenInputIsNotNull() {
        File file = new File("test");
        File expected = new File("TEST");

        assertEquals(upperCaseFile.process(file), expected, "");
    }
}
