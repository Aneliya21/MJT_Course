package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CountFilesTest {

    private Collection<File> input;

    private CountFiles countFiles;

    @BeforeEach
    void setUp() {
        countFiles = new CountFiles();
    }

    @Test
    void testProcessWhenInputIsNull() {
        input = null;
        assertThrows(IllegalArgumentException.class, () -> countFiles.process(input), "IllegalArgumentException should be thrown");
    }

    @Test
    void testProcessWhenInputIsNotNull() {
        File file1 = new File("Test file1");
        File file2 = new File("Test file2");

        input = new ArrayList<>();
        input.add(file1);
        input.add(file2);

        assertEquals(countFiles.process(input), 2, "process(input) should return 2, but was" + countFiles.process(input));
    }
}
