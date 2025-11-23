package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SplitFileTest {

    private SplitFile splitFile;

    @BeforeEach
    void setUp() {
        splitFile = new SplitFile();
    }

    @Test
    void testProcessWhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> splitFile.process(null), "IlleagalArgumentException should be thrown");
    }

    @Test
    void testPrcessWhenInputIsNotNull() {
        File input = new File("test file");

        Set<File> result = splitFile.process(input);

        Set<File> expected = Set.of(
            new File("test"),
            new File("file")
        );

        assertEquals(expected, result, "The split result should contain each word as a separate File");
    }

    @Test
    void testProcessIgnoresEmptyParts() {
        File input = new File("hello   world");

        Set<File> result = splitFile.process(input);

        Set<File> expected = Set.of(
            new File("hello"),
            new File("world")
        );

        assertEquals(expected, result,
            "The split result should ignore empty parts caused by multiple spaces");
    }
}
