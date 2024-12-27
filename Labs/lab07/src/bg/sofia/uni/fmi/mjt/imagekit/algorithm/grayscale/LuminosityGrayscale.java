package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {

    private static final double RED_WEIGHT = 0.21;
    private static final double GREEN_WEIGHT = 0.72;
    private static final double BLUE_WEIGHT = 0.07;

    private static final int MAX_GRAYSCALE_VALUE = 255;
    private static final int MIN_GRAYSCALE_VALUE = 0;

    /**
     * Converts the input image to grayscale using the luminosity method.
     * Formula: 0.21 R + 0.72 G + 0.07 B
     *
     * @param image the original colored image.
     * @return a new BufferedImage that represents the grayscale version of the input image.
     * @throws IllegalArgumentException if the input image is null.
     */
    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Input image cannot be null");
        }
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(image.getRGB(x, y));
                int grayscaleValue = (int) Math.round(RED_WEIGHT * color.getRed() +
                        GREEN_WEIGHT * color.getGreen() +
                        BLUE_WEIGHT * color.getBlue());
                grayscaleValue = Math.max(MIN_GRAYSCALE_VALUE, Math.min(MAX_GRAYSCALE_VALUE, grayscaleValue));

                Color grayscaleColor = new Color(grayscaleValue, grayscaleValue, grayscaleValue);
                grayscaleImage.setRGB(x, y, grayscaleColor.getRGB());
            }
        }
        return grayscaleImage;
    }
}
