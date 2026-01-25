package bg.sofia.uni.fmi.mjt.smarthome.model.device;

public enum TemperatureUnit {
    CELSIUS, FAHRENHEIT;

    private static final double CELSIUS_TO_FAHRENHEIT_SCALE = 9.0 / 5.0;
    private static final double TEMPERATURE_OFFSET = 32.0;
    private static final double FAHRENHEIT_TO_CELSIUS_SCALE = 5.0 / 9.0;

    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * CELSIUS_TO_FAHRENHEIT_SCALE) + TEMPERATURE_OFFSET;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - TEMPERATURE_OFFSET) * FAHRENHEIT_TO_CELSIUS_SCALE;
    }
}
