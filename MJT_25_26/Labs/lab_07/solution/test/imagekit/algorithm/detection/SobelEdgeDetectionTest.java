package Labs.lab_07.solution.test.imagekit.algorithm.detection;

import org.junit.jupiter.api.Test;

import Labs.lab_07.solution.src.imagekit.algorithm.ImageAlgorithm;

import java.awt.image.BufferedImage;
import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SobelEdgeDetectionTest {

    private static class SimpleGrayscale implements ImageAlgorithm {
        @Override
        public BufferedImage process(BufferedImage image) {
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    Color c = new Color(rgb);
                    int gray = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                    Color g = new Color(gray, gray, gray);
                    grayImage.setRGB(x, y, g.getRGB());
                }
            }
            return grayImage;
        }
    }

    private final ImageAlgorithm grayscale = new SimpleGrayscale();

    @Test
    public void testConstructorThrowsForNullGrayscale() {
        assertThrows(IllegalArgumentException.class, () -> new SobelEdgeDetection(null));
    }

    @Test
    public void testProcessThrowsForNullImage() {
        SobelEdgeDetection sobel = new SobelEdgeDetection(grayscale);
        assertThrows(IllegalArgumentException.class, () -> sobel.process(null));
    }

    @Test
    public void testProcessMaintainsImageDimensions() {
        BufferedImage input = new BufferedImage(10, 15, BufferedImage.TYPE_INT_RGB);
        SobelEdgeDetection sobel = new SobelEdgeDetection(grayscale);
        BufferedImage output = sobel.process(input);
        assertEquals(input.getWidth(), output.getWidth());
        assertEquals(input.getHeight(), output.getHeight());
    }

    @Test
    public void testProcessReturnsNonNullImage() {
        BufferedImage input = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        SobelEdgeDetection sobel = new SobelEdgeDetection(grayscale);
        BufferedImage output = sobel.process(input);
        assertNotNull(output);
    }

    @Test
    public void testProcessEdgePixelsRemainUntouched() {
        BufferedImage input = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        SobelEdgeDetection sobel = new SobelEdgeDetection(grayscale);
        BufferedImage output = sobel.process(input);

        for (int x = 0; x < 5; x++) {
            assertEquals(0, new Color(output.getRGB(x, 0)).getRed());
            assertEquals(0, new Color(output.getRGB(x, 4)).getRed());
        }
        for (int y = 0; y < 5; y++) {
            assertEquals(0, new Color(output.getRGB(0, y)).getRed());
            assertEquals(0, new Color(output.getRGB(4, y)).getRed());
        }
    }

    @Test
    public void testProcessPixelsWithinValidRange() {
        BufferedImage input = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        SobelEdgeDetection sobel = new SobelEdgeDetection(grayscale);
        BufferedImage output = sobel.process(input);

        for (int y = 0; y < output.getHeight(); y++) {
            for (int x = 0; x < output.getWidth(); x++) {
                Color c = new Color(output.getRGB(x, y));
                assertTrue(c.getRed() >= 0 && c.getRed() <= 255);
                assertTrue(c.getGreen() >= 0 && c.getGreen() <= 255);
                assertTrue(c.getBlue() >= 0 && c.getBlue() <= 255);
            }
        }
    }
}
