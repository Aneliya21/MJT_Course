package Labs.lab_06.solution.src.file.step;

import java.util.Collection;

import Labs.lab_06.solution.src.file.File;
import Labs.lab_06.solution.src.step.Step;

/**
 * A pipeline step that prints the content of a collection of {@link File} objects
 * to the standard output.
 * <p>
 * This step does not modify the input collection or the files within it. It simply
 * iterates over each {@link File} and prints its content.
 */
public class PrintFiles implements Step<Collection<File>, Collection<File>> {

    /**
     * Prints the content of each {@link File} in the input collection.
     *
     * @param input the collection of files to print
     * @return the same collection of files, unchanged
     *
     * @throws IllegalArgumentException if the input collection is null
     */
    @Override
    public Collection<File> process(Collection<File> input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        for (File f : input) {
            System.out.println(f.getContent());
        }

        return input;
    }

}