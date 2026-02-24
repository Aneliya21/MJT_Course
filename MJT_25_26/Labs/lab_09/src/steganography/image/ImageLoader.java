package Labs.lab_09.src.steganography.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public final class ImageLoader {

    public static BufferedImage load(Path path) {
        try {
            return ImageIO.read(path.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load", e);
        }
    }
}
