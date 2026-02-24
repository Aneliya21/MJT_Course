package Labs.lab_06.solution.test.file;

import org.junit.jupiter.api.Test;

import Labs.lab_06.solution.src.file.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileTest {

    @Test
    void testConstructorWhenContentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new File(null));
    }

    @Test
    void testConstructorWhenContentIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new File(""));
    }

    @Test
    void testConstructorWhenContentIsValid() {
        File file = new File("Test content");

        assertEquals(file.getContent(), "Test content");
    }
}
