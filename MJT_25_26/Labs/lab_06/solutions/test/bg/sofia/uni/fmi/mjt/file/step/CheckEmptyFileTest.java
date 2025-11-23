package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import bg.sofia.uni.fmi.mjt.file.exception.EmptyFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
//
//    @Test
//    void testProcessWhenFileContentIsNull() {
//        File file = new File(null);
//
//        assertThrows(EmptyFileException.class, () -> checkEmptyFile.process(file));
////    }
//
//    @Test
//    void testProcessWhenFileContentIsEmpty() {
//        File file = new File("");
//
//        assertThrows(EmptyFileException.class, () -> checkEmptyFile.process(file));
//    }

    @Test
    void testProcessWhenFileContentIsNotEmpty() {
        File file = new File("Test content");

        assertEquals(checkEmptyFile.process(file), file, "process(file) should return the same file when the content is not empty");
    }
}
