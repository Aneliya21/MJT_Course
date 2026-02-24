package Labs.lab_08.solution.test.order.loader;

import org.junit.jupiter.api.Test;

import Labs.lab_08.solution.src.order.domain.Order;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderLoaderTest {

    @Test
    void testLoadWhenReaderIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> OrderLoader.load(null),
            "Should throw IllegalArgumentException when reader is null");
    }

    @Test
    void testLoadWhenReaderIsValid() {
        String csv = "ORD001,12-03-24,Laptop,ELECTRONICS,999.99,1,999.99,Alice Johnson,Sofia,PAYPAL,COMPLETED\n" +
            "ORD002,05-02-24,Headphones,ELECTRONICS,149.5,2,299.0,Mark Petrov,Plovdiv,PAYPAL,COMPLETED\n";

        try (StringReader reader = new StringReader(csv)) {
            List<Order> orders = OrderLoader.load(reader);

            assertNotNull(orders, "Returned list should not be null");
            assertEquals(2, orders.size(), "Should parse two orders");

            assertEquals("ORD001", orders.get(0).id());
            assertEquals("ORD002", orders.get(1).id());

            assertEquals("Laptop", orders.get(0).product());
            assertEquals("Headphones", orders.get(1).product());
        }
    }

    @Test
    void testLoadWhenIOExceptionOccurs() {
        Reader failingReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Simulated read failure");
            }

            @Override
            public void close() {
            }
        };

        assertThrows(RuntimeException.class,
            () -> OrderLoader.load(failingReader),
            "Expected RuntimeException when Reader throws IOException");
    }

    @Test
    void testLoadSkipsBlankLines() {
        String csv =
            "ORD001,12-03-24,Laptop,ELECTRONICS,999.99,1,999.99,Alice Johnson,Sofia,PAYPAL,COMPLETED\n" +
                "\n" +
                "   \n";

        try (StringReader reader = new StringReader(csv)) {
            List<Order> orders = OrderLoader.load(reader);

            assertEquals(1, orders.size(), "Blank lines should be skipped");

            assertEquals("ORD001", orders.get(0).id());
        }
    }
}
