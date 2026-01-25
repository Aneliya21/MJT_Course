package bg.sofia.uni.fmi.mjt.smarthome.model.user;

import java.nio.file.Path;

public record User(String username, String password, Path userHomeDirectory) {

    public User {
        validateUsername(username);
        validatePassword(password);
        if (userHomeDirectory == null) {
            throw new IllegalArgumentException("Home directory path cannot be null");
        }
    }

    private static void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        // Regex: Only allow alphanumeric, underscores, and hyphens (Safe for folders)
        if (!username.matches("^[a-zA-Z0-9_-]{3,20}$")) {
            throw new IllegalArgumentException("Username must be 3-20 characters (letters, numbers, _ , -)");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        // Basic complexity check: must contain at least one digit
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
    }
}
