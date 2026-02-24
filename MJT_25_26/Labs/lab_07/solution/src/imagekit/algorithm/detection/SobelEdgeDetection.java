package Labs.lab_07.solution.src.imagekit.algorithm.detection;

import java.awt.Color;
import java.awt.image.BufferedImage;

import Labs.lab_07.solution.src.imagekit.algorithm.ImageAlgorithm;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {

    private ImageAlgorithm grayscaleAlgorithm;

    private static final int[][] GX = {
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}
    };

    private static final int[][] GY = {
        {-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}
    };

    private static final int MAX_COLOR_VALUE = 255;

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        if (grayscaleAlgorithm == null) {
            throw new IllegalArgumentException("GrayscaleAlgorithm cannot be null");
        }
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        BufferedImage grayImage = grayscaleAlgorithm.process(image);

        int width = grayImage.getWidth();
        int height = grayImage.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int gx = 0;
                int gy = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = new Color(grayImage.getRGB(x + kx, y + ky)).getRed(); // grayscale so R=G=B
                        gx += GX[ky + 1][kx + 1] * pixel;
                        gy += GY[ky + 1][kx + 1] * pixel;
                    }
                }
                int magnitude = (int) Math.min(MAX_COLOR_VALUE, Math.sqrt(gx * gx + gy * gy));

                Color edgeColor = new Color(magnitude, magnitude, magnitude);
                output.setRGB(x, y, edgeColor.getRGB());
            }
        }
        return output;
    }
}
