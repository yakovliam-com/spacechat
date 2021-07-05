package dev.spaceseries.spacechat.storage;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.model.User;

import java.util.UUID;

public abstract class Storage {

    protected final SpaceChat plugin;

    public Storage(SpaceChat plugin) {
        this.plugin = plugin;
    }

    /**
     * Logs to the storage medium
     *
     * @param data The data to log
     */
    public abstract void log(LogWrapper data, boolean async);

    /**
     * Gets a user
     *
     * @param uuid uuid
     * @return user
     */
    public abstract User getUser(UUID uuid);

    /**
     * Gets a user by their username
     *
     * @param username username
     * @return user
     */
    public abstract User getUser(String username);

    /**
     * Updates a user
     *
     * @param user user
     */
    public abstract void updateUser(User user);

    /**
     * Closes the storage medium
     */
    public abstract void close();
}
