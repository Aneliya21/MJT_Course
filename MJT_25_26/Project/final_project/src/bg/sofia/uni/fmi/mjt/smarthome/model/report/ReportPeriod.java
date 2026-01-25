package bg.sofia.uni.fmi.mjt.smarthome.model.report;

import java.time.LocalDateTime;

public enum ReportPeriod {
    MINUTE,
    HOUR,
    DAY,
    MONTH;

    public LocalDateTime getStartTime() {
        LocalDateTime now = LocalDateTime.now();
        return switch (this) {
            case MINUTE -> now.minusMinutes(1);
            case HOUR   -> now.minusHours(1);
            case DAY    -> now.minusDays(1);
            case MONTH  -> now.minusMonths(1);
        };
    }
}
