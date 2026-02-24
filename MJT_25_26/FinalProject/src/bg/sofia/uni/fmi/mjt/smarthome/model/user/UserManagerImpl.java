package bg.sofia.uni.fmi.mjt.smarthome.model.user;

import bg.sofia.uni.fmi.mjt.smarthome.api.SmartHomeAPI;
import bg.sofia.uni.fmi.mjt.smarthome.api.SmartHomeAPIImpl;
import bg.sofia.uni.fmi.mjt.smarthome.io.UserLoader;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManagerImpl implements UserManager {

    private final Map<String, SmartHomeAPI> userApiSessions;
    // In a real app, this would be a database. For MJT, a file or a Map works.
    private final Map<String, User> registeredUsers;
    private final String baseDataPath = "home";
    private final UserLoader userLoader;

    public UserManagerImpl() {
        this.userApiSessions = new ConcurrentHashMap<>();

        this.userLoader = new UserLoader(Path.of("home", "users.csv"));
        // We load everything into memory immediately
        this.registeredUsers = new ConcurrentHashMap<>(userLoader.loadUsers());
    }

    @Override
    public SmartHomeAPI register(String username, String password) {
        if (registeredUsers.containsKey(username)) {
            throw new IllegalArgumentException("User already exists");
        }

        User newUser = new User(username, password, Path.of("home", username));
        registeredUsers.put(username, newUser);

        // Let the loader handle the file
        userLoader.appendUser(newUser);

        return login(username, password);
    }

    @Override
    public SmartHomeAPI login(String username, String password) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Invalid credentials");
        }
        // return existing session or create a new one
        return userApiSessions.computeIfAbsent(username, name -> {
            // each user gets their own dedicated folder path
            String userPath = baseDataPath + "/" + name;
            return new SmartHomeAPIImpl(userPath);
        });
    }

    @Override
    public void logout(String username) {
        userApiSessions.remove(username);
    }

    private boolean authenticate(String username, String password) {
        // credential validation!!!!!!!!
        return true;
    }
}
