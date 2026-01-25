package bg.sofia.uni.fmi.mjt.smarthome.model.room;

public enum RoomType {
    LIVING_ROOM(20),
    KITCHEN(30),
    BEDROOM(20),
    BATHROOM(10),
    GARAGE(10);

    private int maxDevicesCount;

    RoomType(int maxDevicesCount) {
        this.maxDevicesCount = maxDevicesCount;
    }

    public int getMaxDevicesCount() {
        return maxDevicesCount;
    }
}
