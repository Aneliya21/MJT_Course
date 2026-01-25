package bg.sofia.uni.fmi.mjt.smarthome.model.device.climate;

import bg.sofia.uni.fmi.mjt.smarthome.model.device.DeviceActiveParams;
import bg.sofia.uni.fmi.mjt.smarthome.model.device.DeviceCommand;

public class Heater extends ClimateControl {
    private int powerLevel;

    private static final int MIN_POWER_LEVEL = 1;
    private static final int MAX_POWER_LEVEL = 5;
    private static final double POWER_LEVEL_COEFFICIENT = 0.15;

    public Heater(String id, String name, double powerConsumption) {
        super(id, name, powerConsumption);
    }

    public Heater(String id, String name, double powerConsumption,
                  double currentTemp, double targetTemp) {
        super(id, name, powerConsumption, currentTemp, targetTemp);
    }

    public Heater(String id, String name, double powerConsumption,
                  int powerLevel) {
        super(id, name, powerConsumption);
        validateAndSetPowerLevel(powerLevel);
    }

    public Heater(String id, String name, double powerConsumption,
                  double currentTemp, double targetTemp, int powerLevel) {
        super(id, name, powerConsumption, currentTemp, targetTemp);
        validateAndSetPowerLevel(powerLevel);
    }

    @Override
    public void performAction(DeviceActiveParams params) {
        if (params.getCommand() == DeviceCommand.SET_SPEED) {
            params.getSpeed().ifPresent(s -> this.powerLevel = s);
        } else {
            super.performAction(params);
        }
    }

    @Override
    public double getPowerConsumption() {
        return isOn() ? (super.getPowerConsumption() * (1 + (powerLevel * POWER_LEVEL_COEFFICIENT))) : 0;
    }

    @Override
    public String toFileFormat() {
        return String.format("%s;%d;%s", super.toFileFormat(), powerLevel, "HEATER");
    }

    private void validateAndSetPowerLevel(int powerLevel) {
        if (powerLevel < MIN_POWER_LEVEL || powerLevel > MAX_POWER_LEVEL) {
            throw new IllegalArgumentException("Power level must be more than: " +
                                                MIN_POWER_LEVEL + " and less than: " + MAX_POWER_LEVEL);
        }
        this.powerLevel = powerLevel;
    }
}
