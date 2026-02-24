package Labs.lab_06.solution.src.file.step;

import java.util.Collection;

import Labs.lab_06.solution.src.file.File;
import Labs.lab_06.solution.src.step.Step;

/**
 * A pipeline step that counts the number of {@link File} objects in a collection.
 */
public class CountFiles implements Step<Collection<File>, Integer> {

    /**
     * Returns the number of {@link File} objects in the input collection.
     *
     * @param input the collection of files to count;
     * @return the number of files in the collection
     *
     * @throws IllegalArgumentException if the input collection is null
     */
    @Override
    public Integer process(Collection<File> input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        return input.size();
    }

}