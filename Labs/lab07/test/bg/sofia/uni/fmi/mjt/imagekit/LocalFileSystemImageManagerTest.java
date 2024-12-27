package bg.sofia.uni.fmi.mjt.imagekit;

import bg.sofia.uni.fmi.mjt.imagekit.filesystem.LocalFileSystemImageManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileSystemImageManagerTest {

    private LocalFileSystemImageManager manager;
    private File testImageFile;
    private File testDirectory;
    private File outputImageFile;

    @BeforeEach
    void setUp() throws IOException {
        manager = new LocalFileSystemImageManager();

        testImageFile = new File("test-image.png");
        BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(testImage, "png", testImageFile);

        testDirectory = new File("test-directory");
        testDirectory.mkdir();
        for (int i = 0; i < 3; i++) {
            File tempImage = new File(testDirectory, "image-" + i + ".png");
            ImageIO.write(testImage, "png", tempImage);
        }

        outputImageFile = new File("output-image.png");
    }

    @AfterEach
    void tearDown() {
        if (testImageFile.exists()) {
            testImageFile.delete();
        }
        if (outputImageFile.exists()) {
            outputImageFile.delete();
        }
        if (testDirectory.exists()) {
            for (File file : testDirectory.listFiles()) {
                file.delete();
            }
            testDirectory.delete();
        }
    }

    @Test
    void testLoadImage() throws IOException {
        BufferedImage image = manager.loadImage(testImageFile);
        assertNotNull(image, "Loaded image should not be null");
        assertEquals(100, image.getWidth(), "Image width should be 100");
        assertEquals(100, image.getHeight(), "Image height should be 100");
    }

    @Test
    void testLoadImageFileDoesNotExist() {
        File nonExistentFile = new File("non-existent.png");
        assertThrows(IOException.class, () -> manager.loadImage(nonExistentFile),
                "Expected IOException for non-existent file");
    }

    @Test
    void testLoadImagesFromDirectory() throws IOException {
        var images = manager.loadImagesFromDirectory(testDirectory);
        assertEquals(3, images.size(), "Expected to load 3 images from directory");
    }

    @Test
    void testLoadImagesFromDirectoryInvalidDirectory() {
        File invalidDirectory = new File("invalid-directory");
        assertThrows(IOException.class, () -> manager.loadImagesFromDirectory(invalidDirectory),
                "Expected IOException for invalid directory");
    }

    @Test
    void testSaveImage() throws IOException {
        BufferedImage testImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        manager.saveImage(testImage, outputImageFile);

        assertTrue(outputImageFile.exists(), "Output image file should exist after saving");

        BufferedImage savedImage = ImageIO.read(outputImageFile);
        assertEquals(50, savedImage.getWidth(), "Saved image width should be 50");
        assertEquals(50, savedImage.getHeight(), "Saved image height should be 50");
    }

    @Test
    void testSaveImageFileAlreadyExists() throws IOException {
        BufferedImage testImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(testImage, "png", outputImageFile); // Create the file beforehand

        assertThrows(IOException.class, () -> manager.saveImage(testImage, outputImageFile),
                "Expected IOException for already existing file");
    }
}
