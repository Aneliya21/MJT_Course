package Labs.lab_09.src.steganography.concurrency;

import java.awt.image.BufferedImage;

public class ImageTask {
    private final BufferedImage coverImage;
    private final BufferedImage secretImage;
    private final String outputName;

    public ImageTask(BufferedImage coverImage, BufferedImage secretImage, String outputName) {
        this.coverImage = coverImage;
        this.secretImage = secretImage;
        this.outputName = outputName;
    }

    public BufferedImage getCoverImage() {
        return coverImage;
    }

    public BufferedImage getSecretImage() {
        return secretImage;
    }

    public String getOutputName() {
        return outputName;
    }

}
