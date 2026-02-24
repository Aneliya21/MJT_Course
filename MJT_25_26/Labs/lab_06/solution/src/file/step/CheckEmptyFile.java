package Labs.lab_06.solution.src.file.step;


import Labs.lab_06.solution.src.file.File;
import Labs.lab_06.solution.src.file.exception.EmptyFileException;
import Labs.lab_06.solution.src.step.Step;

/**
 * A pipeline step that validates whether a {@link File} is empty.
 */
public class CheckEmptyFile implements Step<File, File> {

    /**
     * Validates that the input {@link File} is not empty.
     *
     * @param input the file to check
     * @return the same {@link File} if it is not empty
     * @throws EmptyFileException with message "Input file or its content is empty or null"
     *                              if the file is null or if the file content
     *                              is empty or null.
     */
    @Override
    public File process(File input) {
        if (input == null /*||
            input.getContent() == null ||
            input.getContent().isEmpty()*/) {
            throw new EmptyFileException("Input file or its content is empty or null");
        }

        return input;
    }

}