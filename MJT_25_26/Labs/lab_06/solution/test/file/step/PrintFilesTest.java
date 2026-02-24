package Labs.lab_06.solution.test.file.step;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Labs.lab_06.solution.src.file.File;
import Labs.lab_06.solution.src.file.step.PrintFiles;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrintFilesTest {

    private PrintFiles printFiles;

    @BeforeEach
    void setUp() {
        printFiles = new PrintFiles();
    }

    @Test
    void testProcessWhenInputIsNull() {
        Collection<File> files = null;
        assertThrows(IllegalArgumentException.class, () -> printFiles.process(files), "IlleagalArgumentException should be thrown");
    }

    @Test
    void testProcessWhenInputIsNotNull() {
        File file = new File("Test file");
        Collection<File> files = new ArrayList<>();
        files.add(file);

        assertEquals(printFiles.process(files), files, "The PrintFiles step should return the same collection when the input is not null");
    }
}
