package Labs.lab_08.solution.test.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Labs.lab_08.solution.src.order.domain.Category;
import Labs.lab_08.solution.src.order.domain.Order;
import Labs.lab_08.solution.src.order.domain.PaymentMethod;
import Labs.lab_08.solution.src.order.domain.Status;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order("ORD001",
            LocalDate.of(2024, 3, 12),
            "Laptop",
            Category.ELECTRONICS,
            999.99,
            1,
            999.99,
            "Alice Johnson",
            "Sofia",
            PaymentMethod.AMAZON_PAY,
            Status.COMPLETED);
    }

    @Test
    void testOfWhenLineIsNull() {
        assertThrows(IllegalArgumentException.class, () -> Order.of(null), "Should throw IllegalArgumentException when line is null");
    }

    @Test
    void testOfWhenLineIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> Order.of(""), "Should throw IllegalArgumentException when line is blank");
    }

    @Test
    void testOfWhenLineHasLessFieldsThanExpected() {
        assertThrows(IllegalArgumentException.class,
            () -> Order.of("ORD001,12-03-24,Laptop,ELECTRONICS,999.99,1,999.99,Alice Johnson,Sofia,COMPLETED"),
            "Should throw IllegalArgumentException when the size of fields is different from 11");
    }

    @Test
    void testOfWhenLineIsValid() {
        Order result = Order.of("ORD001,12-03-24,Laptop,ELECTRONICS,999.99,1,999.99,Alice Johnson,Sofia,AMAZON_PAY,COMPLETED");
        assertEquals(order, result, "Order should have the same data as the result when method is called");
    }

    @Test
    void testToStringFormatting() {
        String line = "ORD123,15-04-24,Mouse,ELECTRONICS,25.5,2,51.0,Anna Petrova,Sofia,PAYPAL,COMPLETED";
        Order order = Order.of(line);

        String result = order.toString();

        assertTrue(result.contains("date=15-04-24"), "Date should be formatted as dd-MM-yy");
        assertTrue(result.contains("id=ORD123"));
        assertTrue(result.contains("product=Mouse"));
        assertTrue(result.contains("category=ELECTRONICS"));
        assertTrue(result.contains("paymentMethod=PAYPAL"));
        assertTrue(result.contains("status=COMPLETED"));
    }
}
