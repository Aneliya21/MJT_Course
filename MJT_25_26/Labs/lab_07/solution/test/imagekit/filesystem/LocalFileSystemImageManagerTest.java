package Labs.lab_07.solution.test.imagekit.filesystem;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalFileSystemImageManagerTest {

    private final LocalFileSystemImageManager manager = new LocalFileSystemImageManager();

    @Test
    public void testLoadImageThrowsForNullFile() {
        assertThrows(IllegalArgumentException.class, () -> manager.loadImage(null));
    }

    @Test
    public void testLoadImageThrowsForNonExistentFile() {
        File file = new File("nonexistent.png");
        assertThrows(IOException.class, () -> manager.loadImage(file));
    }

    @Test
    public void testLoadImageThrowsForDirectory() throws IOException {
        File tempDir = Files.createTempDirectory("testDir").toFile();
        assertThrows(IOException.class, () -> manager.loadImage(tempDir));
        tempDir.delete();
    }

    @Test
    public void testLoadImageThrowsForUnsupportedFormat() throws IOException {
        File tempFile = File.createTempFile("testImage", ".txt");
        assertThrows(IOException.class, () -> manager.loadImage(tempFile));
        tempFile.delete();
    }

    @Test
    public void testLoadImageLoadsValidPngFile() throws IOException {
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        File tempFile = new File(System.getProperty("java.io.tmpdir"), "testImage.png");
        if (tempFile.exists()) {
            tempFile.delete();
        }

        manager.saveImage(img, tempFile);
        BufferedImage loaded = manager.loadImage(tempFile);
        assertNotNull(loaded);
        assertEquals(img.getWidth(), loaded.getWidth());
        assertEquals(img.getHeight(), loaded.getHeight());

        tempFile.delete();
    }

    @Test
    public void testLoadImagesFromDirectoryThrowsForNullDirectory() {
        assertThrows(IllegalArgumentException.class, () -> manager.loadImagesFromDirectory(null));
    }

    @Test
    public void testLoadImagesFromDirectoryThrowsForNonExistentDirectory() {
        File dir = new File("nonexistentDir");
        assertThrows(IOException.class, () -> manager.loadImagesFromDirectory(dir));
    }

    @Test
    public void testLoadImagesFromDirectoryThrowsForFileInsteadOfDirectory() throws IOException {
        File tempFile = File.createTempFile("testFile", ".png");
        assertThrows(IOException.class, () -> manager.loadImagesFromDirectory(tempFile));
        tempFile.delete();
    }

    @Test
    public void testLoadImagesFromDirectoryLoadsAllImages() throws IOException {
        File tempDir = Files.createTempDirectory("imageDir").toFile();
        BufferedImage img1 = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        BufferedImage img2 = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        File f1 = new File(tempDir, "img1.png");
        File f2 = new File(tempDir, "img2.png");
        manager.saveImage(img1, f1);
        manager.saveImage(img2, f2);

        List<BufferedImage> images = manager.loadImagesFromDirectory(tempDir);
        assertEquals(2, images.size());

        f1.delete();
        f2.delete();
        tempDir.delete();
    }

    @Test
    public void testSaveImageThrowsForNullImage() throws IOException {
        File tempFile = File.createTempFile("test", ".png");
        tempFile.delete();
        assertThrows(IllegalArgumentException.class, () -> manager.saveImage(null, tempFile));
    }

    @Test
    public void testSaveImageThrowsForNullFile() {
        BufferedImage img = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        assertThrows(IllegalArgumentException.class, () -> manager.saveImage(img, null));
    }

    @Test
    public void testSaveImageThrowsForExistingFile() throws IOException {
        BufferedImage img = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        File tempFile = File.createTempFile("test", ".png");
        assertThrows(IOException.class, () -> manager.saveImage(img, tempFile));
        tempFile.delete();
    }

    @Test
    public void testSaveImageThrowsForNonExistentParentDirectory() {
        BufferedImage img = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        File file = new File("nonexistentDir/test.png");
        assertThrows(IOException.class, () -> manager.saveImage(img, file));
    }

    @Test
    public void testSaveImageSuccessfullySavesImage() throws IOException {
        BufferedImage img = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        File tempFile = File.createTempFile("testImage", ".png");
        tempFile.delete();
        manager.saveImage(img, tempFile);
        assertTrue(tempFile.exists());
        tempFile.delete();
    }
}
