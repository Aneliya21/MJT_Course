package Labs.lab_09.src.steganography.concurrency.producer;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

import Labs.lab_09.src.steganography.concurrency.ImageTask;
import Labs.lab_09.src.steganography.exception.SteganographyException;
import Labs.lab_09.src.steganography.image.ImageLoader;

public record EmbedProducer(Path coverPath,
                            Path secretPath,
                            BlockingQueue<ImageTask> queue) implements Runnable {

    @Override
    public void run() {
        BufferedImage cover = ImageLoader.load(coverPath);
        BufferedImage secret = ImageLoader.load(secretPath);

        if (cover == null || secret == null) {
            throw new SteganographyException("Images cannot be null");
        }

        try {
            queue.put(new ImageTask(cover, secret, coverPath.getFileName().toString()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
