package Labs.lab_06.solution.test.file.step;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Labs.lab_06.solution.src.file.File;
import Labs.lab_06.solution.src.file.step.UpperCaseFile;

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
