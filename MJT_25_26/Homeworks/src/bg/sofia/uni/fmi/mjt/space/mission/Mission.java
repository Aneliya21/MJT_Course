package Homeworks.src.bg.sofia.uni.fmi.mjt.space.mission;

import Homeworks.src.bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public record Mission(String id,
                      String company,
                      String location,
                      LocalDate date,
                      Detail detail,
                      RocketStatus rocketStatus,
                      Optional<Double> cost,
                      MissionStatus missionStatus) {

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("EEE MMM dd, yyyy", Locale.ENGLISH);

    public Mission {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Mission id cannot be null or blank");
        }
        if (company == null || company.isBlank()) {
            throw new IllegalArgumentException("Mission company cannot be null or blank");
        }
        if (date == null) {
            throw new IllegalArgumentException("Mission date cannot be null");
        }
        if (detail == null) {
            throw new IllegalArgumentException("Mission detail cannot be null");
        }
        if (rocketStatus == null) {
            throw new IllegalArgumentException("Mission rocketStatus cannot be null");
        }
        if (missionStatus == null) {
            throw new IllegalArgumentException("Mission missionStatus cannot be null");
        }
    }

    public static Mission of(String[] parts) {
        int index = 0;

        String id = parts[index++];
        String company = parts[index++];
        String location = parts[index++];

        LocalDate date = LocalDate.parse(
            parts[index++].replace("\"", ""),
            DateTimeFormatter.ofPattern("EEE MMM dd, yyyy", Locale.ENGLISH)
        );

        String[] detailParts = parts[index++].split("\\|");
        Detail detail = new Detail(
            detailParts[0].trim(),
            detailParts.length > 1 ? detailParts[1].trim() : ""
        );

        String rocketStatusStr = parts[index++];

        RocketStatus rocketStatus = Arrays.stream(RocketStatus.values())
            .filter(rs -> rs.toString().equals(rocketStatusStr))
            .findFirst()
            .orElseThrow();

        String costStr = parts[index++].replace("\"", "").trim();
        Optional<Double> cost = costStr.isEmpty()
            ? Optional.empty()
            : Optional.of(Double.parseDouble(costStr));

        String missionStatusStr = parts[index++];

        MissionStatus missionStatus = Arrays.stream(MissionStatus.values())
            .filter(ms -> ms.toString().equalsIgnoreCase(missionStatusStr))
            .findFirst()
            .orElseThrow();

        return new Mission(
            id, company, location, date,
            detail, rocketStatus, cost, missionStatus
        );
    }

}