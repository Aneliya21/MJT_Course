package Labs.lab_08.solution.src.order.domain;

public enum PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    AMAZON_PAY,
    GIFT_CARD;

    public static PaymentMethod parse(String text) {
        if (text == null) return null;

        return PaymentMethod.valueOf(
            text.trim().toUpperCase().replace(" ", "_")
        );
    }
}
