package Labs.lab_07.solution.src.imagekit.algorithm.grayscale;

import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {

    private static final double RED_WEIGHT = 0.21;
    private static final double GREEN_WEIGHT = 0.72;
    private static final double BLUE_WEIGHT = 0.07;

    private static final int MIN_COLOR_VALUE = 0;
    private static final int MAX_COLOR_VALUE = 255;

    private static final int SHIFT_HIGH = 16;
    private static final int SHIFT_MID = 8;
    private static final int BYTE_MASK = 0xFF;

    public LuminosityGrayscale() {
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }

        final int width  = image.getWidth();
        final int height = image.getHeight();

        BufferedImage grayImage = createTargetImage(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int grayRgb = processPixel(rgb);
                grayImage.setRGB(x, y, grayRgb);
            }
        }

        return grayImage;
    }

    private static BufferedImage createTargetImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    private static int processPixel(int rgb) {
        int[] channels = extractChannels(rgb);
        int lum = computeLuminosity(channels[0], channels[1], channels[2]);
        lum = clamp(lum);
        return packGrayRGB(lum);
    }

    private static int[] extractChannels(int rgb) {
        int r = (rgb >> SHIFT_HIGH) & BYTE_MASK;
        int g = (rgb >> SHIFT_MID)  & BYTE_MASK;
        int b = rgb & BYTE_MASK;
        return new int[] { r, g, b };
    }

    private static int computeLuminosity(int r, int g, int b) {
        return (int) Math.round(RED_WEIGHT * r + GREEN_WEIGHT * g + BLUE_WEIGHT * b);
    }

    private static int packGrayRGB(int lum) {
        return (lum << SHIFT_HIGH) | (lum << SHIFT_MID) | lum;
    }

    private static int clamp(int value) {
        if (value < MIN_COLOR_VALUE) return MIN_COLOR_VALUE;
        if (value > MAX_COLOR_VALUE) return MAX_COLOR_VALUE;
        return value;
    }
}
