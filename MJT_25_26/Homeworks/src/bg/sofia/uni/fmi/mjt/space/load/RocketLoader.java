package Homeworks.src.bg.sofia.uni.fmi.mjt.space.load;

import Homeworks.src.bg.sofia.uni.fmi.mjt.space.rocket.Rocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RocketLoader implements Loader {

    private static final int FIELDS_MAX_COUNT = 4;

    public RocketLoader() {
    }

    @Override
    public List<Rocket> load(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);

        return br.lines()
            .skip(1)
            .map(line -> {
                try {
                    return parseRocketLine(line);
                } catch (Exception e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private Rocket parseRocketLine(String line) {
        String[] parts = line.split(",", -1);

        int index = 0;

        String id = parts[index++];
        String name = parts[index++];

        String wikiStr = parts[index++].trim();
        Optional<String> wiki =
            wikiStr.isEmpty() ? Optional.empty() : Optional.of(wikiStr);

        Optional<Double> height;
        if (parts.length <= index || parts[index].trim().isEmpty()) {
            height = Optional.empty();
        } else {
            String h = parts[index].trim().replace(" m", "");
            height = Optional.of(Double.parseDouble(h));
        }

        return new Rocket(id, name, wiki, height);
    }
}
