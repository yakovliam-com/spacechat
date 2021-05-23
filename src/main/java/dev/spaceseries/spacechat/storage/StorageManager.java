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
     * Initializes storage
     */
    public StorageManager() {
        // get active storage type
        String using = Config.STORAGE_USE.get(Config.get());

        // if type, etc....
        if (using.equalsIgnoreCase("sqlite")) {
            current = new SqliteStorage();
        } else if (using.equalsIgnoreCase("mysql")) {
            current = new MysqlStorage();
        } else {
            SpaceChat.getInstance().getLogger().severe("Unknown storage medium '" + using + "'. The plugin is unable to function correctly.");
            current = new EmptyStorage();
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
