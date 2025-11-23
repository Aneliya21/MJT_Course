package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBusImpl implements EventBus {

    private final Map<Class<? extends Event<?>>, Collection<Subscriber<?>>> subscribers = new HashMap<>();

    private final Map<Class<? extends Event<?>>, Collection<Event<?>>> eventLogs = new HashMap<>();

    public EventBusImpl() {
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        validateEventType(eventType);
        validateSubscriber(subscriber);

        Collection<Subscriber<?>> subs = subscribers.get(eventType);
        if (subs == null) {
            subs = new ArrayList<>();
            subscribers.put(eventType, subs);
        }

        if (!subs.contains(subscriber)) {
            subs.add(subscriber);
        }
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
        throws MissingSubscriptionException {

        validateEventType(eventType);
        validateSubscriber(subscriber);

        Collection<Subscriber<?>> subs = subscribers.get(eventType);
        if (subs == null || !subs.remove(subscriber)) {
            throw new MissingSubscriptionException("subscriber is not subscribed for this event type");
        }

    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        validateEvent(event);

        Class<?> eventType = event.getClass();

        Collection<Event<?>> logs = eventLogs.get(eventType);
        if (logs == null) {
            logs = new ArrayList<>();
            eventLogs.put((Class<? extends Event<?>>) eventType, logs);
        }
        logs.add(event);

        Collection<Subscriber<?>> subs = subscribers.get(eventType);
        if (subs != null) {
            for (Subscriber<?> s : subs) {
                Subscriber<T> sub = (Subscriber<T>) s;
                sub.onEvent(event);
            }
        }
    }

    @Override
    public void clear() {
        subscribers.clear();
        eventLogs.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from,
                                                       Instant to) {
        validateEventType(eventType);
        validateInstant(from);
        validateInstant(to);

        if (from.equals(to)) {
            return Collections.unmodifiableCollection(new ArrayList<Event<?>>());
        }

        Collection<Event<?>> logs = eventLogs.get(eventType);
        if (logs == null) {
            return Collections.unmodifiableCollection(new ArrayList<Event<?>>());
        }

        List<Event<?>> result = new ArrayList<>();
        for (Event<?> e : logs) {
            Instant ts = e.getTimestamp();
            if ((ts.equals(from) || ts.isAfter(from)) && ts.isBefore(to)) {
                result.add(e);
            }
        }

        result.sort((a, b) -> {
            int cmp = a.getTimestamp().compareTo(b.getTimestamp());
            if (cmp != 0) return cmp;
            return Integer.compare(a.getPriority(), b.getPriority());
        });

        return Collections.unmodifiableCollection(result);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        validateEventType(eventType);

        Collection<Subscriber<?>> subs = subscribers.get(eventType);
        if (subs == null) {
            return Collections.unmodifiableCollection(new ArrayList<Subscriber<?>>());
        }
        return Collections.unmodifiableCollection(new ArrayList<>(subs));
    }

    private <T extends Event<?>> void validateEventType(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
    }

    private <T extends Event<?>> void validateSubscriber(Subscriber<? super T> subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("subscriber cannot be null");
        }
    }

    private <T extends Event<?>> void validateEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException("event cannot be null");
        }
    }

    private void validateInstant(Instant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("Time cannot be null");
        }
    }

}
