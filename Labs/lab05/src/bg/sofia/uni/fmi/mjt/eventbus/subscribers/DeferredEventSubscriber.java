package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

public class DeferredEventSubscriber<T extends Event<?>> implements Subscriber<T>, Iterable<T> {

    private final Queue<T> eventsQueue;

    public DeferredEventSubscriber() {
        this.eventsQueue = new PriorityQueue<>(new EventComparator<>());
    }

    private static class EventComparator<T extends Event<?>> implements Comparator<T> {
        @Override
        public int compare(T event1, T event2) {
            int priorityComparison = Integer.compare(event1.getPriority(), event2.getPriority());

            if (priorityComparison != 0) {
                return priorityComparison;
            }

            return event1.getTimestamp().compareTo(event2.getTimestamp());
        }
    }

    /**
     * Stores an event for processing at a later time.
     *
     * @param event the event to be processed
     * @throws IllegalArgumentException if the event is null
     */
    @Override
    public void onEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        eventsQueue.offer(event);
    }

    /**
     * Provides an iterator for the unprocessed events. The iterator should return events sorted by
     * their priority in descending order. Events with equal priority are ordered in ascending order
     * of their timestamps.
     *
     * @return an iterator for the unprocessed events
     */
    @Override
    public Iterator<T> iterator() {
        return eventsQueue.iterator();
    }

    /**
     * Checks if there are unprocessed events.
     *
     * @return true if there are unprocessed events, false otherwise
     */
    public boolean isEmpty() {
        return eventsQueue.isEmpty();
    }
}
