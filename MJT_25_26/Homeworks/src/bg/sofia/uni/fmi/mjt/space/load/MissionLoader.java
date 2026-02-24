package Homeworks.src.bg.sofia.uni.fmi.mjt.space.load;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

import Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission.Mission;

public class MissionLoader implements Loader {

    public MissionLoader() {
    }

    @Override
    public List<Mission> load(Reader reader) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            return bufferedReader.lines()
                .skip(1)
                .map(line -> splitCSV(line))
                .map(Mission::of)
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Could not load mission dataset");
        }
    }

}
