package dev.spaceseries.spacechat.storage;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.storage.impl.empty.EmptyStorage;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.MysqlStorage;
import dev.spaceseries.spacechat.storage.impl.sql.file.sqlite.SqliteStorage;

public class StorageManager {

    /**
     * The current storage medium
     */
    private final Storage current;

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
        String using = Config.STORAGE_USE.get(plugin.getSpaceChatConfig().getConfig());

        // if type, etc....
        if (using.equalsIgnoreCase("sqlite")) {
            current = new SqliteStorage(plugin);
        } else if (using.equalsIgnoreCase("mysql")) {
            current = new MysqlStorage(plugin);
        } else {
            plugin.getLogger().severe("Unknown storage medium '" + using + "'. The plugin is unable to function correctly.");
            current = new EmptyStorage(plugin);
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
