package Labs.lab_09.src.steganography;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Labs.lab_09.src.steganography.concurrency.ImageTask;
import Labs.lab_09.src.steganography.concurrency.PoisonPill;
import Labs.lab_09.src.steganography.concurrency.consumer.EmbedConsumer;
import Labs.lab_09.src.steganography.concurrency.consumer.ExtractConsumer;
import Labs.lab_09.src.steganography.concurrency.producer.EmbedProducer;
import Labs.lab_09.src.steganography.concurrency.producer.ExtractProducer;

public class ImageCodecImpl implements ImageCodec {

    private static final int NUM_CONSUMERS = Runtime.getRuntime().availableProcessors();

    @Override
    public void embedPNGImages(String coverSourceDirectory, String secretSourceDirectory, String outputDirectory) {
        try {
            Path[] coverFiles = listPngFiles(coverSourceDirectory);
            Path[] secretFiles = listPngFiles(secretSourceDirectory);

            if (coverFiles.length != secretFiles.length) {
                throw new IllegalArgumentException("Number of cover and secret images must be the same");
            }

            createDirectory(outputDirectory);

            BlockingQueue<ImageTask> queue = new LinkedBlockingQueue<>();

            startConsumers(queue, NUM_CONSUMERS, outputDirectory, true);
            startProducers(queue, coverFiles, secretFiles);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed during embedding", e);
        }
    }

    @Override
    public void extractPNGImages(String sourceDirectory, String outputDirectory) {
        try {
            Path[] embeddedFiles = listPngFiles(sourceDirectory);

            createDirectory(outputDirectory);

            BlockingQueue<ImageTask> queue = new LinkedBlockingQueue<>();

            startConsumers(queue, NUM_CONSUMERS, outputDirectory, false);
            startProducers(queue, embeddedFiles);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed during extraction", e);
        }
    }

    private static Path[] listPngFiles(String directory) throws IOException {
        return Files.list(Paths.get(directory))
            .filter(p -> p.toString().toLowerCase().endsWith(".png"))
            .sorted()
            .toArray(Path[]::new);
    }

    private static void createDirectory(String directory) throws IOException {
        Files.createDirectories(Paths.get(directory));
    }

    private static void startConsumers(BlockingQueue<ImageTask> queue,
                                       int numConsumers,
                                       String outputDir,
                                       boolean embed) throws InterruptedException {
        Thread[] consumers = new Thread[numConsumers];
        for (int i = 0; i < numConsumers; i++) {
            consumers[i] = new Thread(embed
                ? new EmbedConsumer(queue, outputDir)
                : new ExtractConsumer(queue, outputDir));
            consumers[i].start();
        }

        new Thread(() -> {
            try {
                for (Thread consumer : consumers) {
                    consumer.join();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static void startProducers(BlockingQueue<ImageTask> queue,
                                       Path[] coverFiles,
                                       Path[] secretFiles) throws InterruptedException {
        Thread[] producers = new Thread[coverFiles.length];
        for (int i = 0; i < coverFiles.length; i++) {
            producers[i] = new Thread(new EmbedProducer(coverFiles[i], secretFiles[i], queue));
            producers[i].start();
        }

        for (Thread producer : producers) {
            producer.join();
        }

        for (int i = 0; i < NUM_CONSUMERS; i++) {
            queue.put(new PoisonPill());
        }
    }

    private static void startProducers(BlockingQueue<ImageTask> queue,
                                       Path[] embeddedFiles) throws InterruptedException {
        Thread[] producers = new Thread[embeddedFiles.length];
        for (int i = 0; i < embeddedFiles.length; i++) {
            producers[i] = new Thread(new ExtractProducer(embeddedFiles[i], queue));
            producers[i].start();
        }

        for (Thread producer : producers) {
            producer.join();
        }

        for (int i = 0; i < NUM_CONSUMERS; i++) {
            queue.put(new PoisonPill());
        }
    }
}
