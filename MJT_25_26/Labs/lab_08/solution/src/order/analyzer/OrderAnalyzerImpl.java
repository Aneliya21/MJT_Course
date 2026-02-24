package Labs.lab_08.solution.src.order.analyzer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import Labs.lab_08.solution.src.order.domain.Category;
import Labs.lab_08.solution.src.order.domain.Order;
import Labs.lab_08.solution.src.order.domain.PaymentMethod;
import Labs.lab_08.solution.src.order.domain.Status;

public class OrderAnalyzerImpl implements OrderAnalyzer {

    private static final double SUSPICIOUS_CUSTOMER_SALES_VALUE = 100.0;
    private static final int SUSPICIOUS_CUSTOMER_ORDERS_COUNT = 3;

    private List<Order> orders;

    public OrderAnalyzerImpl(List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("Orders cannot be null");
        }
        this.orders = orders;
    }

    @Override
    public List<Order> allOrders() {
        return orders.stream().toList();
    }

    @Override
    public List<Order> ordersByCustomer(String customer) {
        if (customer == null || customer.isBlank()) {
            throw new IllegalArgumentException("Customer cannot be null or blank");
        }

        return orders.stream()
            .filter(order -> order.customerName().equals(customer))
            .toList();
    }

    @Override
    public Map.Entry<LocalDate, Long> dateWithMostOrders() {
        return orders.stream()
            .collect(Collectors.groupingBy(Order::date, Collectors.counting()))
            .entrySet()
            .stream()
            .max((e1, e2) -> {
                int cmp = e1.getValue().compareTo(e2.getValue());
                if (cmp != 0) {
                    return cmp;
                } else {
                    return e2.getKey().compareTo(e1.getKey());
                }
            })
            .orElse(null);
    }

    @Override
    public List<String> topNMostOrderedProducts(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N - number of orders, cannot be negative");
        }
        if (n == 0) {
            return List.of();
        }

        return orders.stream()
            .collect(Collectors.groupingBy(Order::product, Collectors.counting()))
            .entrySet()
            .stream()
            .sorted((e1, e2) -> {
                int cmp = e2.getValue().compareTo(e1.getValue());
                if (cmp != 0) return cmp;
                return e1.getKey().compareTo(e2.getKey());
            })
            .limit(n)
            .map(Map.Entry::getKey)
            .toList();
    }

    @Override
    public Map<Category, Double> revenueByCategory() {
        return orders.stream()
            .collect(Collectors.groupingBy(
                Order::category,
                Collectors.summingDouble(Order::totalSales)
            ));
    }

    @Override
    public Set<String> suspiciousCustomers() {
        return orders.stream()
            .filter(order -> order.status() == Status.CANCELLED && order.totalSales() < SUSPICIOUS_CUSTOMER_SALES_VALUE)
            .collect(Collectors.groupingBy(Order::customerName, Collectors.counting()))
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > SUSPICIOUS_CUSTOMER_ORDERS_COUNT)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    @Override
    public Map<Category, PaymentMethod> mostUsedPaymentMethodForCategory() {
        return orders.stream()
            .collect(Collectors.groupingBy(
                Order::category,
                Collectors.collectingAndThen(
                    Collectors.groupingBy(
                        Order::paymentMethod,
                        Collectors.counting()
                    ),
                    map -> map.entrySet()
                        .stream()
                        .max((e1, e2) -> {
                            int cmp = e1.getValue().compareTo(e2.getValue());
                            if (cmp != 0) return cmp;
                            return e2.getKey().name().compareTo(e1.getKey().name());
                        })
                        .map(Map.Entry::getKey)
                        .orElse(null)
                )
            ));
    }

    @Override
    public String locationWithMostOrders() {
        return orders.stream()
            .collect(Collectors.groupingBy(Order::customerLocation, Collectors.counting()))
            .entrySet()
            .stream()
            .max((e1, e2) -> {
                int cmp = e1.getValue().compareTo(e2.getValue());
                if (cmp != 0) return cmp;
                return e2.getKey().compareTo(e1.getKey());
            })
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    @Override
    public Map<Category, Map<Status, Long>> groupByCategoryAndStatus() {
        return orders.stream()
            .collect(Collectors.groupingBy(
                Order::category,
                Collectors.groupingBy(
                    Order::status,
                    Collectors.counting()
                )
            ));
    }
}
