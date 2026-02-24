package Labs.lab_08.solution.src.order.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

public record Order(String id,
                    LocalDate date,
                    String product,
                    Category category,
                    double price,
                    int quantity,
                    double totalSales,
                    String customerName,
                    String customerLocation,
                    PaymentMethod paymentMethod,
                    Status status) {

    private static final int ID = 0;
    private static final int DATE = 1;
    private static final int PRODUCT = 2;
    private static final int CATEGORY = 3;
    private static final int PRICE = 4;
    private static final int QUANTITY = 5;
    private static final int TOTAL_SCALES = 6;
    private static final int CUSTOMER_NAME = 7;
    private static final int CUSTOMER_LOCATION = 8;
    private static final int PAYMENT_METHOD = 9;
    private static final int STATUS = 10;

    private static final int FIELDS_SIZE = 11;

    public static Order of(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Line cannot be null or blank");
        }

        String[] tokens = line.split(",", -1);
        if (tokens.length < FIELDS_SIZE) {
            throw new IllegalArgumentException("CSV line does not contain all required fields");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        List<String> fields = IntStream.range(0, tokens.length)
            .mapToObj(i -> tokens[i].trim())
            .toList();

        return new Order(
            fields.get(ID),
            LocalDate.parse(cleanDate(fields.get(DATE)), formatter),
            fields.get(PRODUCT),
            Category.parse(fields.get(CATEGORY).toUpperCase()),
            Double.parseDouble(fields.get(PRICE)),
            Integer.parseInt(fields.get(QUANTITY)),
            Double.parseDouble(fields.get(TOTAL_SCALES)),
            fields.get(CUSTOMER_NAME),
            fields.get(CUSTOMER_LOCATION),
            PaymentMethod.parse(fields.get(PAYMENT_METHOD).toUpperCase()),
            Status.parse(fields.get(STATUS))
        );
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        return ("Order[id=%s, date=%s, product=%s, category=%s, price=%s, quantity=%d, " +
            "totalSales=%s, customerName=%s, customerLocation=%s, paymentMethod=%s, status=%s]")
            .formatted(
                id,
                date.format(formatter),
                product,
                category,
                price,
                quantity,
                totalSales,
                customerName,
                customerLocation,
                paymentMethod,
                status
            );
    }

    private static String cleanDate(String raw) {
        if (raw == null) {
            return null;
        }

        raw = raw.trim();

        if (raw.toLowerCase().startsWith("Date")) {
            int index = raw.indexOf(' ');
            int indexColon = raw.indexOf(':');

            if (indexColon != -1) {
                raw = raw.substring(indexColon + 1).trim();
            } else if (index != -1) {
                raw = raw.substring(index + 1).trim();
            }
        }

        return raw;
    }
}
