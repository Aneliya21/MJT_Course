package Labs.lab_09.src.steganography.concurrency.consumer;

import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;

import Labs.lab_09.src.steganography.concurrency.ImageTask;
import Labs.lab_09.src.steganography.concurrency.PoisonPill;
import Labs.lab_09.src.steganography.image.ImageEmbedder;
import Labs.lab_09.src.steganography.image.ImageSaver;

public record EmbedConsumer(BlockingQueue<ImageTask> queue,
                            String outputDir) implements Runnable {

    @Override
    public void run() {
        while (true) {
            ImageTask task = null;
            try {
                task = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (task instanceof PoisonPill) {
                break;
            }

            BufferedImage result =
                ImageEmbedder.embed(task.getCoverImage(), task.getSecretImage());

            ImageSaver.save(result, outputDir, task.getOutputName());
        }
    }
}
