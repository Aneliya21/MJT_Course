package Labs.lab_08.solution.src.order.domain;

public enum Category {
    ELECTRONICS, CLOTHING, FOOTWEAR, HOME_APPLIANCES, BOOKS;

    public static Category parse(String text) {
        if (text == null) return null;

        return Category.valueOf(
            text.trim().toUpperCase().replace(" ", "_")
        );
    }
}
