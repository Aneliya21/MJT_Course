package Labs.lab_08.solution.src.order.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import Labs.lab_08.solution.src.order.domain.Order;

public class OrderLoader {

    /**
     * Returns a list of orders read from the source Reader.
     *
     * @param reader the Reader with orders
     * @throws IllegalArgumentException if the reader is null
     */
    public static List<Order> load(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        }

        List<Order> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(reader)) {

            String line;

            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) {
                    result.add(Order.of(line));
                }
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error reading orders", ex);
        }

        return result;
    }
}