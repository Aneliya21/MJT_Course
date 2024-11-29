package bg.sofia.uni.fmi.mjt.eventbus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

public class EventBusImpl implements EventBus {

    private final Map<Class<? extends Event<?>>, List<Subscriber<?>>> subscribersMap;
    private final Map<Class<? extends Event<?>>, List<Event<?>>> eventLogs;

    public EventBusImpl() {
        this.subscribersMap = new ConcurrentHashMap<>();
        this.eventLogs = new ConcurrentHashMap<>();
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type and subscriber cannot be null");
        }

        List<Subscriber<?>> subscribers = subscribersMap.get(eventType);
        if (subscribers == null) {
            subscribers = new CopyOnWriteArrayList<>();
            subscribersMap.put(eventType, subscribers);
        }

        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
        throws MissingSubscriptionException {

        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type and subscriber cannot be null");
        }

        List<Subscriber<?>> subscribers = subscribersMap.get(eventType);

        if (subscribers == null || !subscribers.remove(subscriber)) {
            throw new MissingSubscriptionException("Subscriber not subscribed to the event type");
        }
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        List<Event<?>> logs = eventLogs.get(event.getClass());

        if (logs == null) {
            logs = new ArrayList<>();
            eventLogs.put((Class<? extends Event<?>>) event.getClass(), logs);
        }
        logs.add(event);

        List<Subscriber<?>> subscribers = subscribersMap.get(event.getClass());

        if (subscribers != null) {
            for (Subscriber<?> subscriber : subscribers) {
                ((Subscriber<? super T>) subscriber).onEvent(event);
            }
        }
    }

    @Override
    public void clear() {
        subscribersMap.clear();
        eventLogs.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (eventType == null || from == null || to == null) {
            throw new IllegalArgumentException("Event type and timestamps cannot be null");
        }

        if (!eventLogs.containsKey(eventType)) {
            return Collections.emptyList();
        }

        List<Event<?>> logs = eventLogs.get(eventType);
        List<Event<?>> result = new ArrayList<>();

        for (Event<?> event : logs) {
            Instant timestamp = event.getTimestamp();
            if (!timestamp.isBefore(from) && timestamp.isBefore(to)) {
                result.add(event);
            }
        }

        sortEventsByTimestamp(result);

        return Collections.unmodifiableList(result);
    }

    /**
     * Sorts the events in ascending order based on their timestamps.
     */
    private void sortEventsByTimestamp(List<Event<?>> events) {
        int n = events.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (events.get(j).getTimestamp().isAfter(events.get(j + 1).getTimestamp())) {
                    Event<?> temp = events.get(j);
                    events.set(j, events.get(j + 1));
                    events.set(j + 1, temp);
                }
            }
        }
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }

        List<Subscriber<?>> subscribers = subscribersMap.get(eventType);

        if (subscribers == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableCollection(subscribers);
    }
}
