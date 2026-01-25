package bg.sofia.uni.fmi.mjt.smarthome.model.user;

import bg.sofia.uni.fmi.mjt.smarthome.api.SmartHomeAPI;

public interface UserManager {

    SmartHomeAPI register(String username, String password);

    SmartHomeAPI login(String username, String password);

    void logout(String username);

}
