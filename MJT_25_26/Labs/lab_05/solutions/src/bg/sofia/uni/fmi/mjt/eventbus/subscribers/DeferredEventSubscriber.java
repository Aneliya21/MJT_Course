package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

public class DeferredEventSubscriber<T extends Event<?>> implements Subscriber<T>, Iterable<T> {

    private static final Comparator<Event<?>> EVENT_COMPARATOR = new Comparator<Event<?>>() {
        @Override
        public int compare(Event<?> e1, Event<?> e2) {
            int byPriority = Integer.compare(e1.getPriority(), e2.getPriority());
            if (byPriority != 0) {
                return byPriority;
            }
            return e1.getTimestamp().compareTo(e2.getTimestamp());
        }
    };

    private final Queue<T> queue = new PriorityQueue<T>(11, new Comparator<T>() {
        @Override
        public int compare(T a, T b) {
            return EVENT_COMPARATOR.compare(a, b);
        }
    });

    public DeferredEventSubscriber() {
    }

    /**
     * Store an event for processing at a later time.
     *
     * @param event the event to be processed
     * @throws IllegalArgumentException if the event is null
     */
    @Override
    public void onEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        queue.add(event);
    }

    /**
     * Get an iterator for the unprocessed events. The iterator should provide the events sorted
     * by priority, with higher-priority events first (lower priority number = higher priority).
     * For events with equal priority, earlier events (by timestamp) come first.
     *
     * @return an iterator for the unprocessed events
     */
    @Override
    public Iterator<T> iterator() {
        List<T> snapshot = new ArrayList<>(queue);
        snapshot.sort(new Comparator<T>() {
            @Override
            public int compare(T a, T b) {
                return EVENT_COMPARATOR.compare(a, b);
            }
        });

        return new DeferredIterator(snapshot);
    }

    /**
     *
     * Check if there are unprocessed events.
     *
     * @return true if there are unprocessed events, false otherwise
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    private class DeferredIterator implements Iterator<T> {
        private final Iterator<T> it;
        private T lastReturned = null;

        DeferredIterator(List<T> snapshot) {
            this.it = snapshot.iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public T next() {
            lastReturned = it.next();
            return lastReturned;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException("next() has not been called or remove() already called");
            }
            it.remove();
            queue.remove(lastReturned);
            lastReturned = null;
        }
    }

}