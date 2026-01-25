package bg.sofia.uni.fmi.mjt.smarthome.model.room;

import bg.sofia.uni.fmi.mjt.smarthome.exception.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.smarthome.model.device.Device;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RoomManagerImpl implements RoomManager {
    private final Map<String, Room> rooms = new HashMap<>();

    @Override
    public void addRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        rooms.putIfAbsent(room.getId(), room);
    }

    @Override
    public void removeRoom(String roomId) {
        if (roomId == null || roomId.isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        rooms.remove(roomId);
    }

    @Override
    public Room getRoomById(String roomId) {
        if (roomId == null || roomId.isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        return rooms.get(roomId);
    }

    @Override
    public Device findDeviceById(String deviceId) {
        return rooms.values().stream()
            .map(room -> room.getDeviceById(deviceId))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> new DeviceNotFoundException("Device not found: " + deviceId));
    }

    @Override
    public Map<String, Room> getAllRooms() {
        return rooms;
    }

    @Override
    public double calculateTotalPowerConsumption() {
        return rooms.values().stream()
            .flatMapToDouble(room -> room.getDevices().stream()
                .mapToDouble(Device::getPowerConsumption))
            .sum();
    }

    public void applyGlobalEnergySaver() {
        rooms.values().forEach(Room::performEnergySaver);
    }
}
