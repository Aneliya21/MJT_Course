package bg.sofia.uni.fmi.mjt.smarthome.server;

import bg.sofia.uni.fmi.mjt.smarthome.model.report.ReportPeriod;
import bg.sofia.uni.fmi.mjt.smarthome.model.report.log.PowerLog;

import java.time.LocalDateTime;
import java.util.List;

public class EnergyCalculator {

    //returns total Watt-minutes!!
    public double calculate(List<PowerLog> logs, ReportPeriod period) {
        LocalDateTime cutoff = period.getStartTime();

        return logs.stream()
            .filter(log -> log.timestamp().isAfter(cutoff))
            .mapToDouble(PowerLog::watts)
            .sum();
    }
}
