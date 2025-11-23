package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.time.Instant;
import java.util.Objects;

public class SimpleEvent<T extends Payload<?>> implements Event<T> {

    private final Instant timestamp;
    private final int priority;
    private final String source;
    private final T payload;

    public SimpleEvent(int priority, String source, T payload) {
        this(Instant.now(), priority, source, payload);
    }

    public SimpleEvent(Instant timestamp, int priority, String source, T payload) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("source cannot be null or blank");
        }
        if (payload == null) {
            throw new IllegalArgumentException("payload cannot be null");
        }

        this.timestamp = Objects.requireNonNull(timestamp, "timestamp, cannot be null");
        this.priority = priority;
        this.source = source;
        this.payload = payload;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return String.format(
            "%s[timestamp=%s, priority=%d, source=%s, payload=%s]",
            this.getClass().getSimpleName(),
            timestamp,
            priority,
            source,
            payload
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SimpleEvent<?> other)) {
            return false;
        }

        return priority == other.priority
            && Objects.equals(timestamp, other.timestamp)
            && Objects.equals(source, other.source)
            && Objects.equals(payload, other.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, priority, source, payload);
    }
}
