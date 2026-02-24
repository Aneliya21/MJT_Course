package bg.sofia.uni.fmi.mjt.smarthome.tasks;

import bg.sofia.uni.fmi.mjt.smarthome.api.SmartHomeAPI;

public class PowerMonitoringTask implements Runnable {

    private final SmartHomeAPI api;

    public PowerMonitoringTask(SmartHomeAPI api) {
        this.api = api;
    }

    @Override
    public void run() {
        // 1. Get current consumption from the API
        double currentPower = api.getTotalConsumption(null); // Or a specific method for current draw

        // 2. The API will internally call the logger to append this to the log file
        // Or the task can call a logger.appendPowerLog() method directly.
        System.out.println("[Background Task] Recording power: " + currentPower + "W");
    }
}