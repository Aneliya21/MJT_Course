package Homeworks.src.bg.sofia.uni.fmi.mjt.space.load;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public interface Loader {

    List<?> load(Reader reader) throws IOException;

    default String[] splitCSV(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                quoted = !quoted;
            } else if (c == ',' && !quoted) {
                tokens.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        tokens.add(current.toString());
        return tokens.toArray(new String[0]);
    }
}
