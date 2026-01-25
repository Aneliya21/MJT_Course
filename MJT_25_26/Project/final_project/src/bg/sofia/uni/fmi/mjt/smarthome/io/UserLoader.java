package bg.sofia.uni.fmi.mjt.smarthome.io;

import bg.sofia.uni.fmi.mjt.smarthome.model.user.User;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class UserLoader {
    private final Path registryPath;

    public UserLoader(Path registryPath) {
        this.registryPath = registryPath;
    }

    public Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>();
        if (!Files.exists(registryPath)) return users;

        try (var reader = Files.newBufferedReader(registryPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];
                    users.put(username, new User(username, password, Path.of("home", username)));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load user registry", e);
        }
        return users;
    }

    public synchronized void appendUser(User user) {
        try (var writer = Files.newBufferedWriter(registryPath,
            StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(user.username() + ";" + user.password());
            writer.newLine();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save user to registry", e);
        }
    }

}
