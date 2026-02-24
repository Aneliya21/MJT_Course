package Labs.lab_07.solution.src.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {

    public LocalFileSystemImageManager() {
    }

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("ImageFile cannot be null");
        }
        if (!imageFile.exists()) {
            throw new IOException("ImageFile should exist");
        }
        if (!imageFile.isFile()) {
            throw new IOException("ImageFile should be a regular file");
        }
        validateFileFormat(imageFile);

        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Failed to decode image");
        }

        BufferedImage rgbImage = new BufferedImage(
            image.getWidth(),
            image.getHeight(),
            BufferedImage.TYPE_INT_RGB
        );

        rgbImage.getGraphics().drawImage(image, 0, 0, null);

        return rgbImage;
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("ImagesDirectory cannot be null");
        }
        if (!imagesDirectory.exists()) {
            throw new IOException("ImagesDirectory should exist");
        }
        if (!imagesDirectory.isDirectory()) {
            throw new IOException("ImagesDirectory should be a directory");
        }

        File[] files = imagesDirectory.listFiles();
        List<BufferedImage> images = new ArrayList<>(files.length);

        for (File file : files) {
            if (file.isDirectory()) continue;

            BufferedImage img = loadImage(file);
            images.add(img);
        }
        return images;
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        if (imageFile == null) {
            throw new IllegalArgumentException("ImageFile cannot be null");
        }
        if (imageFile.exists()) {
            throw new IOException("File already exists");
        }

        File parent = imageFile.getParentFile();
        if (parent == null || (!parent.exists() || !parent.isDirectory())) {
            throw new IOException("Parent directory does not exist.");
        }

        String format = validateFileFormat(imageFile);
        boolean written = ImageIO.write(image, format, imageFile);

        if (!written) {
            throw new IOException("Failed to write image");
        }
    }

    private static String validateFileFormat(File imageFile) throws IOException {
        String fileName = imageFile.getName().toLowerCase().trim();

        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "jpg";
        } else if (fileName.endsWith(".png")) {
            return "png";
        } else if (fileName.endsWith(".bmp")) {
            return "bmp";
        } else {
            throw new IOException("ImageFile should be in JPEG, PNG, or BMP format");
        }
    }
}
