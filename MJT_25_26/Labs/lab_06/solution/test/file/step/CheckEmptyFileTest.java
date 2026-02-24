package Labs.lab_06.solution.test.file.step;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Labs.lab_06.solution.src.file.exception.EmptyFileException;
import Labs.lab_06.solution.src.file.step.CheckEmptyFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckEmptyFileTest {

    private CheckEmptyFile checkEmptyFile;

    @BeforeEach
    void setUp() {
        checkEmptyFile = new CheckEmptyFile();
    }

    @Test
    void testProcessWhenFileIsNull() {
        File file = null;

        assertThrows(EmptyFileException.class, () -> checkEmptyFile.process(file), "EmptyFileException should be thrown");
    }

    @Test
    void testProcessWhenFileContentIsNotEmpty() {
        File file = new File("Test content");

        assertEquals(checkEmptyFile.process(file), file, "process(file) should return the same file when the content is not empty");
    }
}
