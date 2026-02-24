package Labs.lab_09.src.steganography.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public final class ImageSaver {

    public static void save(BufferedImage image, String dir, String name) {
        try {
            ImageIO.write(image, "png", new File(dir, name));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save", e);
        }
    }
}
