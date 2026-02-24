package bg.sofia.uni.fmi.mjt.smarthome.tasks;

import bg.sofia.uni.fmi.mjt.smarthome.api.SmartHomeAPI;
import bg.sofia.uni.fmi.mjt.smarthome.model.room.Room;
import java.util.Collection;
import java.util.Map;

public class AutoSaveTask implements Runnable {
    private final SmartHomeAPI api;

    public AutoSaveTask(SmartHomeAPI api) {
        this.api = api;
    }

    @Override
    public void run() {
        // This single call handles the entire persistence logic
        api.persistAllRooms();
    }
}