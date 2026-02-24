package Labs.lab_08.solution.src.order.domain;

public enum Status {
    COMPLETED,
    PENDING,
    CANCELLED;

    public static Status parse(String text) {
        if (text == null) return null;

        return Status.valueOf(
            text.trim().toUpperCase()
        );
    }
}
