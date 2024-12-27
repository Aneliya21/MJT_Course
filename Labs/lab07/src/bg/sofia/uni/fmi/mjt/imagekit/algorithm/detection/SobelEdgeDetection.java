package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {

    private final ImageAlgorithm grayscaleAlgorithm;

    private static final int[][] SOBEL_X = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    private static final int[][] SOBEL_Y = {
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}
    };

    private static final int MAX_GRAYSCALE_VALUE = 255;

    /**
     * Constructs a SobelEdgeDetection with a given grayscale algorithm.
     *
     * @param grayscaleAlgorithm the grayscale algorithm to convert the image to grayscale.
     */
    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    /**
     * Applies edge detection to the given grayscale image using Sobel operator.
     *
     * @param image the input grayscale image.
     * @return a BufferedImage with the detected edges.
     */
    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Input image cannot be null");
        }
        BufferedImage grayscaleImage = grayscaleAlgorithm.process(image);

        int width = grayscaleImage.getWidth();
        int height = grayscaleImage.getHeight();
        BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int gx = 0;
                int gy = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color color = new Color(grayscaleImage.getRGB(x + i, y + j));
                        int gray = color.getRed();
                        gx += SOBEL_X[i + 1][j + 1] * gray;
                        gy += SOBEL_Y[i + 1][j + 1] * gray;
                    }
                }
                int g = (int) Math.round(Math.min(MAX_GRAYSCALE_VALUE, Math.sqrt(gx * gx + gy * gy)));
                Color edgeColor = new Color(g, g, g);
                edgeImage.setRGB(x, y, edgeColor.getRGB());
            }
        }
        return edgeImage;
    }
}
