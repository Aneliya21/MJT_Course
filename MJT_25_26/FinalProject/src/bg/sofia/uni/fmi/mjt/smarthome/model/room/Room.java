package bg.sofia.uni.fmi.mjt.smarthome.model.room;

import bg.sofia.uni.fmi.mjt.smarthome.exception.TooManyDevicesException;
import bg.sofia.uni.fmi.mjt.smarthome.model.device.Device;
import bg.sofia.uni.fmi.mjt.smarthome.model.device.DeviceActiveParams;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Room {
    private String id;
    private String name;
    private RoomType type;
    private Map<String, Device> devices;
    private boolean energySaverRegime;

    private static final int MAX_POWER_CONSUMPTION_IN_ENERGY_SAVER_REGIME = 1_000;

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
        devices = new HashMap<>();
        energySaverRegime = false;
    }

    public Room(String id, String name, RoomType type, boolean energySaverRegime) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.devices = new HashMap<>();
        this.energySaverRegime = energySaverRegime;
    }

    public void addDevice(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("Device cannot be null");
        }

        if (devices.size() >= type.getMaxDevicesCount()) {
            throw new TooManyDevicesException("Room " + name + " of type " + type +
                " cannot have more than " + type.getMaxDevicesCount() + " devices");
        }

        devices.putIfAbsent(device.getDeviceID(), device);
    }

    public void removeDevice(String deviceID) {
        devices.remove(deviceID);
    }

    public double calculateTotalPowerConsumption() {
        return devices.values().stream()
            .mapToDouble(Device::getPowerConsumption)
            .sum();
    }

    public void performActionOnDevice(DeviceActiveParams params, String deviceId) {
        devices.get(deviceId).performAction(params);
    }

    //may be better to happen automatically i the method addDevice or inside a background Task
    public void performEnergySaver() {
        if (energySaverRegime) {
            devices.values().stream()
                .filter(Device::isOn)
                .filter(d -> d.getPowerConsumption() > MAX_POWER_CONSUMPTION_IN_ENERGY_SAVER_REGIME)
                .forEach(Device::turnOff);
        }
    }

    // TBD
    public Device getDeviceById(String deviceId) {
        return null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<Device> getDevices() {
        return devices.values();
    }

    // other getters if needed
}
