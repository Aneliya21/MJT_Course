package Labs.lab_05.src.eventbus.events;

public interface Payload<T> {

    /**
     * @return the size of the payload
     */
    int getSize();

    /**
     * @return the payload
     */
    T getPayload();

}