package bg.sofia.uni.fmi.mjt.smarthome.io;

import bg.sofia.uni.fmi.mjt.smarthome.model.device.Device;
import bg.sofia.uni.fmi.mjt.smarthome.model.room.Room;
import bg.sofia.uni.fmi.mjt.smarthome.model.room.RoomManager;
import bg.sofia.uni.fmi.mjt.smarthome.model.room.RoomManagerImpl;
import bg.sofia.uni.fmi.mjt.smarthome.utility.DeviceFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

//to create hierarchy: /home/user/rooms/
public class SmartHomeLogger {

    private final Path userPath;
    private final Path roomsPath;

    public SmartHomeLogger(String userHomePath) {
        this.userPath = Paths.get(userHomePath);
        this.roomsPath = userPath.resolve("rooms");
    }

    public void setupDirectoryStructure() {
        try {
            // Creates the home/user/ folder and the rooms/ subfolder
            Files.createDirectories(roomsPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create user directory structure", e);
        }
    }

    public synchronized void saveRoom(Room room) {
        // Saves to: home/user/rooms/roomId_roomName.txt
        Path roomFile = roomsPath.resolve(room.getId() + "_" + room.getName() + ".txt");

        try (var writer = Files.newBufferedWriter(roomFile)) {
            for (Device device : room.getDevices()) {
                // id;name;type;isOn;isAsleep;power
                writer.write(device.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void saveGlobalConfig(double price, boolean energySaver) {
        Path configFile = userPath.resolve("config.txt");
        Properties props = new Properties();

        // Convert values to strings for storage
        props.setProperty("electricity.price", String.valueOf(price));
        props.setProperty("energy.saver.active", String.valueOf(energySaver));

        try (var writer = Files.newBufferedWriter(configFile)) {
            props.store(writer, "User Smart Home Configuration");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save global config", e);
        }
    }

    public synchronized RoomManager loadHouseConfiguration() {
        RoomManager roomManager = new RoomManagerImpl();

        // 1. Load Global Config (Price/Energy Saver)
        //loadGlobalConfig();

        // 2. Load Rooms and Devices
        try (var files = Files.list(roomsPath)) { // Lists all .txt files in rooms/
            files.filter(path -> path.toString().endsWith(".txt"))
                .forEach(path -> {
                    Room room = parseRoomFile(path);
                    roomManager.addRoom(room);
                });
        } catch (IOException e) {
            // If the folder is empty/missing, we just return an empty RoomManager
        }

        return roomManager;
    }

    private Room parseRoomFile(Path path) {
        // Extract name/id from filename: "kitchen-01_Kitchen.txt"
        String filename = path.getFileName().toString().replace(".txt", "");
        String[] roomParts = filename.split("_");
        String roomId = roomParts[0];
        String roomName = roomParts[1];

        Room room = new Room(roomId, roomName);

        try (var reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Format: id;name;type;isOn;isAsleep;consumption
                String[] p = line.split(";");
                Device device = DeviceFactory.create(
                    p[2], p[0], p[1],
                    Boolean.parseBoolean(p[3]),
                    Boolean.parseBoolean(p[4]),
                    Double.parseDouble(p[5])
                );
                room.addDevice(device);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return room;
    }

    public synchronized void appendPowerLog(double totalWatts) {
        Path logFile = userPath.resolve("power_consumption.log");
        String entry = System.currentTimeMillis() + ";" + totalWatts + System.lineSeparator();

        try {
            // StandardOpenOption.APPEND ensures we don't delete old data
            Files.writeString(logFile, entry,
                java.nio.file.StandardOpenOption.CREATE,
                java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not log power consumption", e);
        }
    }
}
