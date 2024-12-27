package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {

    /**
     * Loads a single image from the given file path.
     *
     * @param imageFile the file containing the image.
     * @return the loaded BufferedImage.
     * @throws IllegalArgumentException if the file is null.
     * @throws IOException              if the file does not exist, is not a regular file,
     *                                  or is not in one of the supported formats.
     */
    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("Image file cannot be null");
        }
        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new IOException("The specified file does not exist or is not a regular file");
        }

        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Unsupported image format or corrupted file");
        }
        return image;
    }

    /**
     * Loads all images from the specified directory.
     *
     * @param imagesDirectory the directory containing the images.
     * @return A list of BufferedImages representing the loaded images.
     * @throws IllegalArgumentException if the directory is null.
     * @throws IOException              if the directory does not exist, is not a directory,
     *                                  or contains files that are not in one of the supported formats.
     */
    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("Images directory cannot be null");
        }
        if (!imagesDirectory.exists() || !imagesDirectory.isDirectory()) {
            throw new IOException("The specified directory does not exist or is not a directory");
        }

        File[] files = imagesDirectory.listFiles();
        if (files == null) {
            throw new IOException("Failed to list files in the directory");
        }

        List<BufferedImage> images = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                try {
                    BufferedImage image = loadImage(file);
                    images.add(image);
                } catch (IOException e) {
                    throw new IOException("Unsupported or corrupted image format in file: " + file.getName(), e);
                }
            }
        }
        return images;
    }

    /**
     * Saves the given image to the specified file path.
     *
     * @param image     the image to save.
     * @param imageFile the file to save the image to.
     * @throws IllegalArgumentException if the image or file is null.
     * @throws IOException              if the file already exists or the parent directory does not exist.
     */
    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("Image or image file cannot be null");
        }
        if (imageFile.exists()) {
            throw new IOException("The specified file already exists");
        }
        File parentDir = imageFile.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Failed to create parent directory");
        }

        String format = getFileExtension(imageFile.getName());
        if (format == null || (!format.equalsIgnoreCase("png")
                && !format.equalsIgnoreCase("jpg")
                && !format.equalsIgnoreCase("bmp"))) {
            throw new IOException("Unsupported file format: " + format);
        }

        boolean saved = ImageIO.write(image, format, imageFile);
        if (!saved) {
            throw new IOException("Failed to save the image to the specified file");
        }
    }

    /**
     * Helper method to extract the file extension from a file name.
     *
     * @param fileName the name of the file.
     * @return the file extension, or null if no extension is found.
     */
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return null;
        }
        return fileName.substring(dotIndex + 1).toLowerCase();
    }
}
