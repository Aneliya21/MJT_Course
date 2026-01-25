package bg.sofia.uni.fmi.mjt.smarthome.model.device.climate;

import bg.sofia.uni.fmi.mjt.smarthome.model.device.DeviceActiveParams;
import bg.sofia.uni.fmi.mjt.smarthome.model.device.DeviceCommand;

public class AirConditioner extends ClimateControl {

    // TBD Add degrees!!
    private int fanSpeed;

    private static final int MIN_FAN_SPEED = 1;
    private static final int MAX_FAN_SPEED = 3;
    private static final double FAN_SPEED_COEFFICIENT = 0.1;

    public AirConditioner(String id, String name, double powerConsumption) {
        super(id, name, powerConsumption);
        this.fanSpeed = MIN_FAN_SPEED;
    }

    public AirConditioner(String id, String name, double powerConsumption,
                          double currentTemp, double targetTemp) {
        super(id, name, powerConsumption, currentTemp, targetTemp);
    }

    //it calls super(...) - it resets your temperatures to the defaults!!!
    public AirConditioner(String id, String name, double powerConsumption, int fanSpeed) {
        super(id, name, powerConsumption);
        validateAndSetFanSpeed(fanSpeed);
    }

    public AirConditioner(String id, String name, double powerConsumption,
                          double currentTemp, double targetTemp, int fanSpeed) {
        super(id, name, powerConsumption, currentTemp, targetTemp);
        validateAndSetFanSpeed(fanSpeed);
    }

    @Override
    public void performAction(DeviceActiveParams params) {
        if (params.getCommand() == DeviceCommand.SET_SPEED) {
            params.getSpeed().ifPresent(s -> this.fanSpeed = s);
        } else {
            super.performAction(params);
        }
    }

    @Override
    public double getPowerConsumption() {
        return isOn() ? (super.getPowerConsumption() * (1 + (fanSpeed * FAN_SPEED_COEFFICIENT))) : 0;
    }

    @Override
    public String toFileFormat() {
        // Format: BaseInfo+ClimateInfo;FanSpeed;DeviceType
        return String.format("%s;%d;%s", super.toFileFormat(), fanSpeed, "AIR_CONDITIONER");
    }

    private void validateAndSetFanSpeed(int fanSpeed) {
        if (fanSpeed < MIN_FAN_SPEED || fanSpeed > MAX_FAN_SPEED) {
            throw new IllegalArgumentException("Fan speed must be more than: " +
                MIN_FAN_SPEED + " and less than: " + MAX_FAN_SPEED);
        }
        this.fanSpeed = fanSpeed;
    }
}
