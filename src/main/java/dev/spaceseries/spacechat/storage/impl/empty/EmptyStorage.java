package dev.spaceseries.spacechat.storage.impl.empty;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.model.User;
import dev.spaceseries.spacechat.storage.Storage;
import dev.spaceseries.spacechat.storage.StorageInitializationException;

import java.util.UUID;

public class EmptyStorage extends Storage {

    public EmptyStorage(SpaceChat plugin) throws StorageInitializationException {
        super(plugin);
    }

    @Override
    public void log(LogWrapper data, boolean async) {
    }

    /**
     * Gets a user
     *
     * @param uuid uuid
     * @return user
     */
    @Override
    public User getUser(UUID uuid) {
        return null;
    }

    /**
     * Gets a user by their username
     *
     * @param username username
     * @return user
     */
    @Override
    public User getUser(String username) {
        return null;
    }

    /**
     * Updates a user
     *
     * @param user user
     */
    @Override
    public void updateUser(User user) {
    }

    @Override
    public void close() {
    }
}
