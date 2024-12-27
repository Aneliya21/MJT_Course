package bg.sofia.uni.fmi.mjt.imagekit;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LuminosityGrayscaleTest {

    private LuminosityGrayscale luminosityGrayscale;

    @BeforeEach
    void setUp() {
        luminosityGrayscale = new LuminosityGrayscale();
    }

    @Test
    void testProcessValidImage() {
        BufferedImage colorImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        colorImage.setRGB(0, 0, new Color(255, 0, 0).getRGB()); // Red
        colorImage.setRGB(0, 1, new Color(0, 255, 0).getRGB()); // Green
        colorImage.setRGB(1, 0, new Color(0, 0, 255).getRGB()); // Blue
        colorImage.setRGB(1, 1, new Color(255, 255, 255).getRGB()); // White

        BufferedImage grayscaleImage = luminosityGrayscale.process(colorImage);

        assertNotNull(grayscaleImage, "Grayscale image should not be null");
        assertEquals(2, grayscaleImage.getWidth(), "Width should match the original image");
        assertEquals(2, grayscaleImage.getHeight(), "Height should match the original image");

        int grayTopLeft = new Color(grayscaleImage.getRGB(0, 0)).getRed();
        int grayTopRight = new Color(grayscaleImage.getRGB(0, 1)).getRed();
        int grayBottomLeft = new Color(grayscaleImage.getRGB(1, 0)).getRed();
        int grayBottomRight = new Color(grayscaleImage.getRGB(1, 1)).getRed();

        assertEquals(54, grayTopLeft, "Red should be converted to grayscale correctly");
        assertEquals(182, grayTopRight, "Green should be converted to grayscale correctly");
        assertEquals(18, grayBottomLeft, "Blue should be converted to grayscale correctly");
        assertEquals(255, grayBottomRight, "White should be converted to grayscale correctly");
    }

    @Test
    void testProcessNullImageThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> luminosityGrayscale.process(null),
                "Processing a null image should throw IllegalArgumentException");
    }

    @Test
    void testProcessUniformImage() {
        BufferedImage uniformImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                uniformImage.setRGB(x, y, new Color(255, 255, 255).getRGB());
            }
        }

        BufferedImage grayscaleImage = luminosityGrayscale.process(uniformImage);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                int pixel = new Color(grayscaleImage.getRGB(x, y)).getRed();
                assertEquals(255, pixel, "All pixels should remain white for a uniform white input");
            }
        }
    }

    @Test
    void testProcessBlackImage() {
        BufferedImage blackImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                blackImage.setRGB(x, y, new Color(0, 0, 0).getRGB());
            }
        }

        BufferedImage grayscaleImage = luminosityGrayscale.process(blackImage);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                int pixel = new Color(grayscaleImage.getRGB(x, y)).getRed();
                assertEquals(0, pixel, "All pixels should remain black for a uniform black input");
            }
        }
    }
}
