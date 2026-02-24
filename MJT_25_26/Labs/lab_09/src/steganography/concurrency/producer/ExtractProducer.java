package Labs.lab_09.src.steganography.concurrency.producer;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

import Labs.lab_09.src.steganography.concurrency.ImageTask;
import Labs.lab_09.src.steganography.image.ImageLoader;

public record ExtractProducer(Path sourcePath,
                              BlockingQueue<ImageTask> queue) implements Runnable {

    @Override
    public void run() {
        BufferedImage embeddedImage = ImageLoader.load(sourcePath);
        try {
            queue.put(new ImageTask(embeddedImage, null, sourcePath.getFileName().toString()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
