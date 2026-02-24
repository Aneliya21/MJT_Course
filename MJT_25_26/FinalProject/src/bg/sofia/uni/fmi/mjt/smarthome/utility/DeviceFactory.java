package bg.sofia.uni.fmi.mjt.smarthome.utility;

import bg.sofia.uni.fmi.mjt.smarthome.model.device.Device;
import bg.sofia.uni.fmi.mjt.smarthome.model.device.climate.AirConditioner;
import bg.sofia.uni.fmi.mjt.smarthome.model.device.climate.Heater;

public class DeviceFactory {
    public static Device create(String type, String id, String name,
                                boolean isOn, boolean isAsleep, double consumption) {
        Device device = switch (type) {
            case "AirConditioner" -> new AirConditioner(id, name, consumption);
            case "Heater" -> new Heater(id, name, consumption);
            //case "Lamp" -> new Lamp(id, name, consumption);
            // Add other devices here
            default -> throw new IllegalArgumentException("Unknown device type: " + type);
        };

        // Restore the state that isn't in the constructor
        if (isOn) device.turnOn();
        else device.turnOff();
        // Set sleep mode if your device class supports it
        return device;
    }
}