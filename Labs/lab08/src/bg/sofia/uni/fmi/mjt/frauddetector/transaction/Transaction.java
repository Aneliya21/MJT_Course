package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(String transactionID,
                          String accountID,
                          double transactionAmount,
                          LocalDateTime transactionDate,
                          String location,
                          Channel channel) {

    private static final String PEAK_ATTRIBUTE_DELIMITER = ",";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int ATTRIBUTES_NUMBER = 6;
    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int THIRD = 2;
    private static final int FOURTH = 3;
    private static final int FIFTH = 4;
    private static final int SIXTH = 5;

    public static Transaction of(String line) {
        final String[] tokens = line.split(PEAK_ATTRIBUTE_DELIMITER);
        if (tokens.length != ATTRIBUTES_NUMBER) {
            throw new IllegalArgumentException("Invalid transaction line: " + line);
        }
        return new Transaction(tokens[FIRST], tokens[SECOND], Double.parseDouble(tokens[THIRD]),
                LocalDateTime.parse(tokens[FOURTH], DATE_TIME_FORMATTER), tokens[FIFTH],
                Channel.valueOf(tokens[SIXTH].toUpperCase()));
    }
}
