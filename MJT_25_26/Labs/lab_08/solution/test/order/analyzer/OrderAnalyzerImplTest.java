package Labs.lab_08.solution.test.order.analyzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Labs.lab_08.solution.src.order.analyzer.OrderAnalyzerImpl;
import Labs.lab_08.solution.src.order.domain.Category;
import Labs.lab_08.solution.src.order.domain.Order;
import Labs.lab_08.solution.src.order.domain.PaymentMethod;
import Labs.lab_08.solution.src.order.domain.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderAnalyzerImplTest {

    private OrderAnalyzerImpl analyzer;
    private Order order1;
    private Order order2;
    private Order order3;
    private Order order4;
    private Order order5;

    @BeforeEach
    void setUp() {
        order1 = new Order("ORD001", LocalDate.of(2024, 3, 12), "Laptop",
            Category.ELECTRONICS, 999.99, 1, 999.99,
            "Alice", "Sofia", PaymentMethod.PAYPAL, Status.COMPLETED);

        order2 = new Order("ORD002", LocalDate.of(2024, 3, 12), "Laptop",
            Category.ELECTRONICS, 999.99, 1, 999.99,
            "Bob", "Plovdiv", PaymentMethod.CREDIT_CARD, Status.COMPLETED);

        order3 = new Order("ORD003", LocalDate.of(2024, 3, 13), "Book",
            Category.BOOKS, 25, 2, 50,
            "Alice", "Sofia", PaymentMethod.PAYPAL, Status.CANCELLED);

        order4 = new Order("ORD004", LocalDate.of(2024, 3, 13), "Book",
            Category.BOOKS, 25, 1, 25,
            "Alice", "Sofia", PaymentMethod.CREDIT_CARD, Status.CANCELLED);

        order5 = new Order("ORD005", LocalDate.of(2024, 3, 13), "Book",
            Category.BOOKS, 25, 1, 10,
            "Alice", "Sofia", PaymentMethod.GIFT_CARD, Status.CANCELLED);

        analyzer = new OrderAnalyzerImpl(List.of(order1, order2, order3, order4, order5));
    }

    @Test
    void testAllOrders() {
        List<Order> all = analyzer.allOrders();
        assertEquals(5, all.size());
        assertTrue(all.contains(order1));
        assertTrue(all.contains(order5));
    }

    @Test
    void testOrdersByCustomerWhenCustomerIsNull() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.ordersByCustomer(null), "Should throw IllegalArgumentException when customer is null");
    }

    @Test
    void testOrdersByCustomerWhenCustomerIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.ordersByCustomer(""), "Should throw IllegalArgumentException when customer is blank");
    }

    @Test
    void testOrdersByCustomerWhenCustomerIsValid() {
        List<Order> aliceOrders = analyzer.ordersByCustomer("Alice");
        assertEquals(4, aliceOrders.size());
        assertTrue(aliceOrders.contains(order1));
        assertTrue(aliceOrders.contains(order3));
    }

    @Test
    void testDateWithMostOrders() {
        Map.Entry<LocalDate, Long> entry = analyzer.dateWithMostOrders();
        assertNotNull(entry);
        assertEquals(LocalDate.of(2024, 3, 13), entry.getKey());
        assertEquals(3L, entry.getValue());
    }

    @Test
    void testTopNMostOrderedProductsWhenNIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.topNMostOrderedProducts(-1), "Should throw IllegalArgumentException when N is negative");
    }

    @Test
    void testTopNMostOrderedProductsWhenNIsZero() {
        List<String> result = analyzer.topNMostOrderedProducts(0);
        assertTrue(result.isEmpty());
    }

    @Test
    void testTopNMostOrderedProductsWhenNIsPositive() {
        List<String> topProducts = analyzer.topNMostOrderedProducts(2);
        assertEquals(2, topProducts.size());
        assertEquals("Book", topProducts.get(0));
        assertEquals("Laptop", topProducts.get(1));
    }

    @Test
    void testRevenueByCategory() {
        Map<Category, Double> revenue = analyzer.revenueByCategory();
        assertEquals(2, revenue.size());
        assertEquals(1999.98, revenue.get(Category.ELECTRONICS));
        assertEquals(85.0, revenue.get(Category.BOOKS));
    }

    @Test
    void testSuspiciousCustomers() {
        Set<String> suspicious = analyzer.suspiciousCustomers();
        assertEquals(1, suspicious.size());
        assertTrue(suspicious.contains("Alice"));
    }

    @Test
    void testMostUsedPaymentMethodForCategory() {
        Map<Category, PaymentMethod> map = analyzer.mostUsedPaymentMethodForCategory();
        assertEquals(PaymentMethod.PAYPAL, map.get(Category.ELECTRONICS));
        assertEquals(PaymentMethod.CREDIT_CARD, map.get(Category.BOOKS));
    }

    @Test
    void testLocationWithMostOrders() {
        String location = analyzer.locationWithMostOrders();
        assertEquals("Sofia", location);
    }

    @Test
    void testGroupByCategoryAndStatus() {
        Map<Category, Map<Status, Long>> grouped = analyzer.groupByCategoryAndStatus();
        assertEquals(2, grouped.size());
        assertEquals(2L, grouped.get(Category.ELECTRONICS).get(Status.COMPLETED));
        assertEquals(3L, grouped.get(Category.BOOKS).get(Status.CANCELLED));
    }
}
