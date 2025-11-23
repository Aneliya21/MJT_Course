package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public final class SimplePayload<T> implements Payload<T> {

    private final T payload;

    public SimplePayload(T payload) {
        this.payload = payload;
    }

    @Override
    public int getSize() {
        if (payload == null) {
            return 0;
        }

        if (payload instanceof Collection) {
            return ((Collection<?>) payload).size();
        }

        if (payload instanceof Map) {
            return ((Map<?, ?>) payload).size();
        }

        if (payload instanceof CharSequence) {
            return Array.getLength(payload);
        }

        return 1;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return String.format("%s[size=%d, payload=%s]",
            this.getClass().getSimpleName(),
            getSize(),
            Objects.toString(payload, "null"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SimplePayload)) {
            return false;
        }

        SimplePayload<?> that = (SimplePayload<?>) o;
        return Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(payload);
    }
}
