package Labs.lab_09.src.steganography.image;

import java.awt.image.BufferedImage;

import Labs.lab_09.src.steganography.exception.SteganographyException;

public final class ImageEmbedder {

    private static final int RED_SHIFT = 16;
    private static final int GREEN_SHIFT = 8;
    private static final int BLUE_SHIFT = 0;
    private static final int ALPHA_SHIFT = 24;
    private static final int COLOR_MASK = 0xFF;
    private static final int CLEAR_LSB_MASK = 0xFE;

    private ImageEmbedder() { }

    public static BufferedImage embed(BufferedImage cover, BufferedImage secret) {
        int coverPixels = cover.getWidth() * cover.getHeight();
        int secretBitsNeeded = secret.getWidth() * secret.getHeight() * 24; // 24 bits per secret pixel
        if (coverPixels * 3 < secretBitsNeeded) {
            throw new SteganographyException("Cover image too small for full-color secret");
        }

        BufferedImage result = deepCopy(cover);
        int coverIndex = 0;
        int[] secretBits = getSecretBits(secret);

        for (int bit : secretBits) {
            int x = coverIndex % cover.getWidth();
            int y = coverIndex / cover.getWidth();
            int rgb = result.getRGB(x, y);

            int r = (rgb >> RED_SHIFT) & COLOR_MASK;
            int g = (rgb >> GREEN_SHIFT) & COLOR_MASK;
            int b = (rgb >> BLUE_SHIFT) & COLOR_MASK;

            int channel = coverIndex % 3;
            if (channel == 0) r = (r & CLEAR_LSB_MASK) | bit;
            else if (channel == 1) g = (g & CLEAR_LSB_MASK) | bit;
            else b = (b & CLEAR_LSB_MASK) | bit;

            int newRgb = (COLOR_MASK << ALPHA_SHIFT) | (r << RED_SHIFT) | (g << GREEN_SHIFT) | b;
            result.setRGB(x, y, newRgb);

            if (channel == 2) coverIndex++;
        }

        return result;
    }

    private static int[] getSecretBits(BufferedImage secret) {
        int w = secret.getWidth();
        int h = secret.getHeight();
        int[] bits = new int[w * h * 24];
        int index = 0;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = secret.getRGB(x, y);
                for (int shift : new int[]{16, 8, 0}) { // R, G, B
                    int value = (rgb >> shift) & 0xFF;
                    for (int i = 7; i >= 0; i--) {
                        bits[index++] = (value >> i) & 1;
                    }
                }
            }
        }
        return bits;
    }

    private static BufferedImage deepCopy(BufferedImage original) {
        BufferedImage copy = new BufferedImage(
            original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < original.getHeight(); y++)
            for (int x = 0; x < original.getWidth(); x++)
                copy.setRGB(x, y, original.getRGB(x, y));
        return copy;
    }
}
