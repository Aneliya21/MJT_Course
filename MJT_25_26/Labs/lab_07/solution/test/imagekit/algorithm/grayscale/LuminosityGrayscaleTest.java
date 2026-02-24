package Labs.lab_07.solution.test.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LuminosityGrayscaleTest {

    private final LuminosityGrayscale grayscale = new LuminosityGrayscale();

    @Test
    public void testProcessThrowsForNullImage() {
        assertThrows(IllegalArgumentException.class, () -> grayscale.process(null));
    }

    @Test
    public void testProcessMaintainsImageDimensions() {
        BufferedImage input = new BufferedImage(10, 20, BufferedImage.TYPE_INT_RGB);
        BufferedImage output = grayscale.process(input);
        assertEquals(input.getWidth(), output.getWidth());
        assertEquals(input.getHeight(), output.getHeight());
    }

    @Test
    public void testProcessConvertsPixelCorrectly() {
        int r = 100, g = 150, b = 200;
        BufferedImage input = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        input.setRGB(0, 0, new Color(r, g, b).getRGB());

        BufferedImage output = grayscale.process(input);
        int grayRgb = output.getRGB(0, 0);

        int expectedLum = (int) Math.round(0.21 * r + 0.72 * g + 0.07 * b);
        Color grayColor = new Color(grayRgb);
        assertEquals(expectedLum, grayColor.getRed());
        assertEquals(expectedLum, grayColor.getGreen());
        assertEquals(expectedLum, grayColor.getBlue());
    }

    @Test
    public void testProcessClampsBlackPixel() {
        BufferedImage input = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        input.setRGB(0, 0, new Color(0, 0, 0).getRGB());
        BufferedImage output = grayscale.process(input);
        Color gray = new Color(output.getRGB(0, 0));
        assertEquals(0, gray.getRed());
        assertEquals(0, gray.getGreen());
        assertEquals(0, gray.getBlue());
    }

    @Test
    public void testProcessClampsWhitePixel() {
        BufferedImage input = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        input.setRGB(0, 0, new Color(255, 255, 255).getRGB());
        BufferedImage output = grayscale.process(input);
        Color gray = new Color(output.getRGB(0, 0));
        assertEquals(255, gray.getRed());
        assertEquals(255, gray.getGreen());
        assertEquals(255, gray.getBlue());
    }

    @Test
    public void testProcessHandlesMultiplePixels() {
        BufferedImage input = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        input.setRGB(0, 0, new Color(10, 20, 30).getRGB());
        input.setRGB(1, 0, new Color(50, 60, 70).getRGB());
        input.setRGB(0, 1, new Color(100, 110, 120).getRGB());
        input.setRGB(1, 1, new Color(200, 210, 220).getRGB());

        BufferedImage output = grayscale.process(input);

        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                Color c = new Color(output.getRGB(x, y));
                assertEquals(c.getRed(), c.getGreen());
                assertEquals(c.getGreen(), c.getBlue());
            }
        }
    }
}
