package dev.spaceseries.spacechat.storage;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.SpaceChatConfigKeys;
import dev.spaceseries.spacechat.storage.impl.empty.EmptyStorage;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.MysqlStorage;

public class StorageManager {

    /**
     * The current storage medium
     */
    private Storage current;

    /**
     * Plugin
     */
    private final SpaceChat plugin;

    /**
     * Initializes storage
     */
    public StorageManager(SpaceChat plugin) {
        this.plugin = plugin;

        // get active storage type
        String using = SpaceChatConfigKeys.STORAGE_USE.get(plugin.getSpaceChatConfig().getAdapter());

        // if type, etc....
        if (using.equalsIgnoreCase("mysql")) {
            try {
                current = new MysqlStorage(plugin);
            } catch (StorageInitializationException e) {
                e.printStackTrace();
            }
        } else {
            plugin.getLogger().severe("Unknown storage medium '" + using + "'. The plugin is unable to function correctly.");
            try {
                current = new EmptyStorage(plugin);
            } catch (StorageInitializationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns storage
     *
     * @return storage
     */
    public Storage getCurrent() {
        return current;
    }
}
