package bg.sofia.uni.fmi.mjt.smarthome.model.device;

import java.util.Optional;

public class DeviceActiveParams {
    private DeviceCommand command;
    private Double temperature;
    private Integer speed;
    private Integer durationMinutes;

    private static final int MIN_TEMPERATURE_VALUE = 0;
    private static final int MAX_TEMPERATURE_CELSIUS_VALUE = 250;
    private static final int MAX_TEMPERATURE_FAHRENHEIT_VALUE = 500;

    private DeviceActiveParams(DeviceCommand command, Double temperature,
                               Integer speed, Integer durationMinutes) {
        this.command = command;
        this.temperature = temperature;
        this.speed = speed;
        this.durationMinutes = durationMinutes;
    }

    static DeviceActiveParams simpleCommand(DeviceCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Device command cannot be null");
        }
        return new DeviceActiveParams(command, null, null, null);
    }

    static DeviceActiveParams timerCommand(Integer durationMinutes) {
        if (durationMinutes == null) {
            throw new IllegalArgumentException("Timer duration cannot be null");
        }
        return new DeviceActiveParams(DeviceCommand.TIMER_MODE, null, null, durationMinutes);
    }

    static DeviceActiveParams speedCommand(int speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("Speed cannot be less than 0");
        }
        return new DeviceActiveParams(DeviceCommand.SET_SPEED, null, speed, null);
    }

    static DeviceActiveParams temperatureCommand(double temperature, TemperatureUnit temperatureUnit) {
        if (temperature < MIN_TEMPERATURE_VALUE) {
            throw new IllegalArgumentException("Temperature cannot be less than 0");
        }
        if (temperatureUnit == null) {
            throw new IllegalArgumentException("Temperature Unit cannot be null");
        }
        if (temperatureUnit == TemperatureUnit.CELSIUS && temperature > MAX_TEMPERATURE_CELSIUS_VALUE) {
            throw new IllegalArgumentException("Temperature cannot be more than: " +
                MAX_TEMPERATURE_CELSIUS_VALUE + " degrees celsius");
        }
        if (temperatureUnit == TemperatureUnit.FAHRENHEIT && temperature > MAX_TEMPERATURE_FAHRENHEIT_VALUE) {
            throw new IllegalArgumentException("Temperature cannot be more than: " +
                MAX_TEMPERATURE_FAHRENHEIT_VALUE + " degrees fahrenheit");
        }

        return new DeviceActiveParams(DeviceCommand.SET_TEMPERATURE, temperature, null, null);
    }

    public DeviceCommand getCommand() {
        return command;
    }

    public Optional<Double> getTemperature() {
        return Optional.ofNullable(temperature);
    }

    public Optional<Integer> getDuration() {
        return Optional.ofNullable(durationMinutes);
    }

    public Optional<Integer> getSpeed() {
        return Optional.ofNullable(speed);
    }
}
