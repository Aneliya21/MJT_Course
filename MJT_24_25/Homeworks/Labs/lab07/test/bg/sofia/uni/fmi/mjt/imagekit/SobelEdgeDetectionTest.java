package bg.sofia.uni.fmi.mjt.imagekit;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.SobelEdgeDetection;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class SobelEdgeDetectionTest {

    private SobelEdgeDetection sobelEdgeDetection;
    private BufferedImage testImage;

    @BeforeEach
    void setUp() {
        LuminosityGrayscale grayscaleAlgorithm = new LuminosityGrayscale();

        sobelEdgeDetection = new SobelEdgeDetection(grayscaleAlgorithm);

        testImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        testImage.setRGB(0, 0, 0xFFFFFF); // White
        testImage.setRGB(0, 1, 0xCCCCCC); // Light Gray
        testImage.setRGB(0, 2, 0x000000); // Black
        testImage.setRGB(1, 0, 0x777777); // Medium Gray
        testImage.setRGB(1, 1, 0x333333); // Dark Gray
        testImage.setRGB(1, 2, 0x000000); // Black
        testImage.setRGB(2, 0, 0x000000); // Black
        testImage.setRGB(2, 1, 0x000000); // Black
        testImage.setRGB(2, 2, 0x000000); // Black
    }

    @Test
    void testProcessValidImage() {
        BufferedImage edgeDetectedImage = sobelEdgeDetection.process(testImage);

        assertNotNull(edgeDetectedImage, "Edge detected image should not be null");
        assertEquals(testImage.getWidth(), edgeDetectedImage.getWidth(),
                "Width of the edge-detected image should match the original image");
        assertEquals(testImage.getHeight(), edgeDetectedImage.getHeight(),
                "Height of the edge-detected image should match the original image");

        int expectedPixel = edgeDetectedImage.getRGB(1, 1); // Center pixel
        assertNotEquals(0x000000, expectedPixel, "Center pixel should not be black if edges are detected");
    }

    @Test
    void testProcessNullImageThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> sobelEdgeDetection.process(null),
                "Processing a null image should throw IllegalArgumentException");
    }

    @Test
    void testProcessEdgeDetectionOnUniformImage() {
        BufferedImage uniformImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                uniformImage.setRGB(x, y, 0x777777);
            }
        }

        BufferedImage edgeDetectedImage = sobelEdgeDetection.process(uniformImage);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                int pixel = new Color(edgeDetectedImage.getRGB(x, y)).getRed();
                assertEquals(0, pixel, "All pixels in edge-detected image should be black for a uniform input");
            }
        }
    }
}
