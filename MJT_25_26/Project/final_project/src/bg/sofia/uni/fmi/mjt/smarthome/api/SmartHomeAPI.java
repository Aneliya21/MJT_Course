package bg.sofia.uni.fmi.mjt.smarthome.api;

import bg.sofia.uni.fmi.mjt.smarthome.model.device.Device;
import bg.sofia.uni.fmi.mjt.smarthome.model.room.Room;
import bg.sofia.uni.fmi.mjt.smarthome.model.device.DeviceActiveParams;
import bg.sofia.uni.fmi.mjt.smarthome.model.report.ReportPeriod;
import bg.sofia.uni.fmi.mjt.smarthome.model.room.RoomManager;

import java.util.Collection;
import java.util.Map;

public interface SmartHomeAPI {

    // --- Room & Device Management (Delegated to RoomManager) ---

    Map<String, Room> getAllRooms();

    void persistAllRooms();

    void addRoom(Room room);

    void removeRoom(String roomId);

    void addDeviceToRoom(String roomId, Device device);

    void removeDeviceFromRoom(String roomId, String deviceId);

    // --- Device Management ---
    void executeCommand(String deviceId, DeviceActiveParams params);

    Device getDeviceStatus(String deviceId);

    // --- Room & House Discovery ---
    Collection<Device> getDevicesInRoom(String roomId);

    Map<String, Collection<Device>> getAllDevicesByRoom();

    // --- Energy & Statistics ---
    double getTotalConsumption(ReportPeriod period);

    double getConsumptionPrice(ReportPeriod period); // for month

    // --- System Controls ---
    void setEnergySaverMode(boolean active);

    void emergencyStop(); // Turns off everything
}
