package bg.sofia.uni.fmi.mjt.smarthome.api;

import bg.sofia.uni.fmi.mjt.smarthome.io.SmartHomeLogger;
import bg.sofia.uni.fmi.mjt.smarthome.model.device.Device;
import bg.sofia.uni.fmi.mjt.smarthome.model.device.DeviceActiveParams;
import bg.sofia.uni.fmi.mjt.smarthome.model.report.ReportPeriod;
import bg.sofia.uni.fmi.mjt.smarthome.model.room.Room;
import bg.sofia.uni.fmi.mjt.smarthome.model.room.RoomManager;
import bg.sofia.uni.fmi.mjt.smarthome.server.EnergyCalculator;

import java.util.Collection;
import java.util.Map;

public class SmartHomeAPIImpl implements SmartHomeAPI {

    private final RoomManager roomManager;
    private final SmartHomeLogger smartHomeLogger;
    private final EnergyCalculator energyCalculator;
    private final String userHomePath;
    private final double kwhPrice;

    private static final double PRICE_PER_KWH = 0.12; //EUR

//    //TBD
//    public SmartHomeAPIImpl(String userHomePath) {
//        this.userHomePath = userHomePath;
//
//        // 1. init the logger with the specific user path
//        this.smartHomeLogger = new SmartHomeLogger(userHomePath);
//
//        // 2. ensure folders exist on the desk
//        //this.smartHomeLogger.setupDirectoryStructure();
//
//        // 3. load existing data into the RoomManager
//        //this.roomManager = this.smartHomeLogger.loadHouseConfiguration();
//    }

    public SmartHomeAPIImpl(RoomManager roomManager, SmartHomeLogger smartHomeLogger, String userHomePath, double kwhPrice) {
        this.roomManager = roomManager;
        this.smartHomeLogger = smartHomeLogger;
        this.energyCalculator = new EnergyCalculator(); // Internal utility
        this.userHomePath = userHomePath;
        this.kwhPrice = kwhPrice;
    }

    @Override
    public Map<String, Room> getAllRooms() {
        return null;
    }

    @Override
    public void persistAllRooms() {
        // 1. Get all rooms from your roomManager
        Map<String, Room> rooms = roomManager.getAllRooms();

        // 2. Iterate and tell the logger to save each one
        for (Room room : rooms) {
            // This method in your logger is already synchronized!
            logger.saveRoom(room);
        }

        // 3. Also save the global settings while we are at it
        logger.saveGlobalConfig(this.kwhPrice, this.energySaverActive);
    }

    @Override
    public void addRoom(Room room) {

    }

    //should remove the corresponding file from disk
    @Override
    public void removeRoom(String roomId) {

    }

    @Override
    public void addDeviceToRoom(String roomId, Device device) {

    }

    @Override
    public void removeDeviceFromRoom(String roomId, String deviceId) {

    }

    @Override
    public void executeCommand(String deviceId, DeviceActiveParams params) {

    }

    @Override
    public Device getDeviceStatus(String deviceId) {
        return null;
    }

    @Override
    public Collection<Device> getDevicesInRoom(String roomId) {
        return null;
    }

    @Override
    public Map<String, Collection<Device>> getAllDevicesByRoom() {
        return null;
    }

    @Override
    public double getTotalConsumption(ReportPeriod period) {
        return 0;
    }

    @Override
    public double getConsumptionPrice(ReportPeriod period) {
        return 0;
    }

    @Override
    public void setEnergySaverMode(boolean active) {

    }

    @Override
    public void emergencyStop() {

    }
}
