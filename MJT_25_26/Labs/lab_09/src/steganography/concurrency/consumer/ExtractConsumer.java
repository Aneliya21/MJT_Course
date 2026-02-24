package Labs.lab_09.src.steganography.concurrency.consumer;

import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;

import Labs.lab_09.src.steganography.concurrency.ImageTask;
import Labs.lab_09.src.steganography.concurrency.PoisonPill;
import Labs.lab_09.src.steganography.image.ImageExtractor;
import Labs.lab_09.src.steganography.image.ImageSaver;

public record ExtractConsumer(BlockingQueue<ImageTask> queue,
                              String outputDir) implements Runnable {
    @Override
    public void run() {
        while (true) {
            ImageTask task;
            try {
                task = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (task instanceof PoisonPill) {
                break;
            }

            BufferedImage extracted = ImageExtractor.extract(task.getCoverImage());
            ImageSaver.save(extracted, outputDir, task.getOutputName());
        }
    }
}
