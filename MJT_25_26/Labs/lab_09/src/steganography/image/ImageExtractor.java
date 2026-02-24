package Labs.lab_09.src.steganography.image;

import java.awt.image.BufferedImage;

import Labs.lab_09.src.steganography.exception.SteganographyException;

public final class ImageExtractor {

    private static final int METADATA_VALUES_COUNT = 2;
    private static final int BITS_PER_METADATA_VALUE = 12;
    private static final int BITS_PER_PIXEL = 3;
    private static final int PIXELS_PER_METADATA_VALUE =
        BITS_PER_METADATA_VALUE / BITS_PER_PIXEL; // 4
    private static final int METADATA_PIXELS =
        METADATA_VALUES_COUNT * PIXELS_PER_METADATA_VALUE; // 8

    private static final int COLOR_COMPONENT_MASK = 0xFF;
    private static final int LSB_MASK = 0x01;

    private static final int RED_SHIFT = 16;
    private static final int GREEN_SHIFT = 8;
    private static final int BLUE_SHIFT = 0;
    private static final int ALPHA_SHIFT = 24;

    private static final int FIRST_SECRET_MSB_SHIFT = 7;
    private static final int IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;

    private ImageExtractor() {
    }

    public static BufferedImage extract(BufferedImage cover) {
        int coverPixels = cover.getWidth() * cover.getHeight();
        if (coverPixels < METADATA_PIXELS) {
            throw new SteganographyException("Cover image too small to contain metadata");
        }

        int coverIndex = 0;
        int secretWidth = read12Bits(cover, coverIndex);
        coverIndex += PIXELS_PER_METADATA_VALUE;
        int secretHeight = read12Bits(cover, coverIndex);
        coverIndex += PIXELS_PER_METADATA_VALUE;

        BufferedImage secret = new BufferedImage(secretWidth, secretHeight, IMAGE_TYPE);

        for (int y = 0; y < secretHeight; y++) {
            for (int x = 0; x < secretWidth; x++) {

                int[] bits = readBits(cover, coverIndex++);
                int avg = (bits[0] << FIRST_SECRET_MSB_SHIFT) |
                    (bits[1] << FIRST_SECRET_MSB_SHIFT - 1) | (bits[2] << FIRST_SECRET_MSB_SHIFT - 2);
                int rgb = (COLOR_COMPONENT_MASK << ALPHA_SHIFT) |
                    (avg << RED_SHIFT) |
                    (avg << GREEN_SHIFT) |
                    (avg << BLUE_SHIFT);

                secret.setRGB(x, y, rgb);
            }
        }
        return secret;
    }

    private static int read12Bits(BufferedImage img, int startIndex) {
        int value = 0;
        for (int i = 0; i < BITS_PER_METADATA_VALUE; i += BITS_PER_PIXEL) {
            int[] bits = readBits(img, startIndex++);
            value = (value << BITS_PER_PIXEL) | (bits[0] << 2) | (bits[1] << 1) | bits[2];
        }
        return value;
    }

    private static int[] readBits(BufferedImage img, int index) {
        int x = index % img.getWidth();
        int y = index / img.getWidth();

        int rgb = img.getRGB(x, y);

        int r = (rgb >> RED_SHIFT) & COLOR_COMPONENT_MASK;
        int g = (rgb >> GREEN_SHIFT) & COLOR_COMPONENT_MASK;
        int b = (rgb >> BLUE_SHIFT) & COLOR_COMPONENT_MASK;

        return new int[]{
            r & LSB_MASK,
            g & LSB_MASK,
            b & LSB_MASK
        };
    }
}
