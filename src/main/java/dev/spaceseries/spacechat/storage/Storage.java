package dev.spaceseries.spacechat.storage;

import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.model.User;

import java.util.UUID;

public interface Storage {

    /**
     * Logs to the storage medium
     *
     * @param data The data to log
     */
    void log(LogWrapper data, boolean async);

    /**
     * Gets a user
     *
     * @param uuid uuid
     * @return user
     */
    User getUser(UUID uuid);

    /**
     * Gets a user by their username
     *
     * @param username username
     * @return user
     */
    User getUser(String username);

    /**
     * Updates a user
     *
     * @param user user
     */
    void updateUser(User user);

    /**
     * Closes the storage medium
     */
    void close();
}
