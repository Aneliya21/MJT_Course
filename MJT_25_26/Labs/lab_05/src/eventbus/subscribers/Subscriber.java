package Labs.lab_05.src.eventbus.subscribers;

import Labs.lab_05.src.eventbus.events.Event;

public interface Subscriber<T extends Event<?>> {

    /**
     * This method will be called by the EventBus when a new event for the type this subscriber is
     * subscribed to is published.
     *
     * @param event the event that was published
     * @throws IllegalArgumentException if the event is null
     */
    void onEvent(T event);

}