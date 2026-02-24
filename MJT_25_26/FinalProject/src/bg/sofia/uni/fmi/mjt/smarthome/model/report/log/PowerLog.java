package bg.sofia.uni.fmi.mjt.smarthome.model.report.log;

import java.time.LocalDateTime;

public record PowerLog(String deviceId, double watts, LocalDateTime timestamp) {
}
