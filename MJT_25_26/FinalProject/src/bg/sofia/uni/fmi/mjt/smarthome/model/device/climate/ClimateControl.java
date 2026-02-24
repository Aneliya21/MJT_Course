package bg.sofia.uni.fmi.mjt.smarthome.model.device.climate;

import bg.sofia.uni.fmi.mjt.smarthome.model.device.Device;
import bg.sofia.uni.fmi.mjt.smarthome.model.device.DeviceActiveParams;

public abstract class ClimateControl extends Device {
    private double currentTemperature;
    private double targetTemperature;

    private static final double DEFAULT_CURRENT_TEMPERATURE = 22.0;
    private static final double DEFAULT_TARGET_TEMPERATURE = 24.0;

    private static final int MIN_TEMPERATURE_VALUE = 0;
    private static final int MAX_TEMPERATURE_VALUE = 250;

    public ClimateControl(String id, String name, double powerConsumption) {
        super(id, name, powerConsumption);
        this.currentTemperature = DEFAULT_CURRENT_TEMPERATURE;
        this.targetTemperature = DEFAULT_TARGET_TEMPERATURE;
    }

    public ClimateControl(String id, String name, double powerConsumption,
                          double currentTemperature, double targetTemperature) {
        super(id, name, powerConsumption);
        this.currentTemperature = currentTemperature;
        this.targetTemperature = targetTemperature;
    }

    @Override
    public void performAction(DeviceActiveParams params) {
        //handle common climate commands
        switch (params.getCommand()) {
            case SET_TEMPERATURE -> params.getTemperature().ifPresent(this::setTargetTemperature);
            case SLEEP_MODE -> {
                turnOn();
                setTargetTemperature(DEFAULT_TARGET_TEMPERATURE);
            } // other common logic...
            default -> super.performAction(params); //this handles TURN_ON / TURN_OFF
        }
    }

    @Override
    public String toFileFormat() {
        return String.format("%s;%.1f;%.1f", super.toFileFormat(), currentTemperature, targetTemperature);
    }

    @Override
    public double getPowerConsumption() {
        return powerConsumption;
    }

    //maybe some validation here??
    public void setTargetTemperature(double temp) {
        if (temp < MIN_TEMPERATURE_VALUE || temp > MAX_TEMPERATURE_VALUE) {
            throw new IllegalArgumentException("Temperature must be more than: "
                                                + MIN_TEMPERATURE_VALUE + " and less than: " + MAX_TEMPERATURE_VALUE);
        }
        this.targetTemperature = temp;
    }
}
