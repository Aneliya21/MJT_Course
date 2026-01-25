package bg.sofia.uni.fmi.mjt.smarthome.model.device;

public abstract class Device {
    private final String id;
    private final String name;
    private boolean isOn;
    private boolean isAsleep;
    protected final double powerConsumption;

    public Device(String id, String name, double powerConsumption) {
        this.id = id;
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.isOn = false;
    }

    public void turnOn() {
        isOn = true;
    }

    public void turnOff() {
        isOn = false;
    }

    public boolean isOn() {
        return isOn;
    }

    public String getDeviceID() {
        return id;
    }

    public abstract double getPowerConsumption();

    public void performAction(DeviceActiveParams params) {
        switch (params.getCommand()) {
            case TURN_ON -> this.turnOn();
            case TURN_OFF -> this.turnOff();
        }
    }

    public String toFileFormat() {
        return String.format("%s;%s;%b;%.2f", id, name, isOn, powerConsumption);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s", id, name, isOn ? "ON" : "OFF");
    }
}
