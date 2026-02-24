package bg.sofia.uni.fmi.mjt.smarthome.model.room;

import bg.sofia.uni.fmi.mjt.smarthome.model.device.Device;

import java.util.Map;

public interface RoomManager {

    void addRoom(Room room);

    void removeRoom(String roomId);

    Room getRoomById(String roomId);

    Device findDeviceById(String deviceId);

    Map<String, Room> getAllRooms();

    double calculateTotalPowerConsumption();
}
