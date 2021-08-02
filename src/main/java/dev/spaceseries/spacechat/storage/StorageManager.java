package dev.spaceseries.spacechat.storage;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.config.SpaceChatConfigKeys;
import dev.spaceseries.spacechat.storage.impl.empty.EmptyStorage;
import dev.spaceseries.spacechat.storage.impl.json.JsonStorage;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.MysqlStorage;
import org.bukkit.Bukkit;

public class StorageManager {

    /**
     * The current storage medium
     */
    private Storage current;

    /**
     * Initializes storage
     */
    public StorageManager(SpaceChatPlugin plugin) {
        /**
         * Plugin
         */

        // get active storage type
        String using = SpaceChatConfigKeys.STORAGE_USE.get(plugin.getSpaceChatConfig().getAdapter());

        // if type, etc....
        try {
            if (using.equalsIgnoreCase("mysql")) {
                current = new MysqlStorage(plugin);
            } else if (using.equalsIgnoreCase("json")) {
                current = new JsonStorage(plugin);
            } else {
                plugin.getLogger().severe("Unknown storage medium '" + using + "'. The plugin is unable to function correctly.");
                current = new EmptyStorage(plugin);
            }
        } catch (StorageInitializationException e) {
            e.printStackTrace();
            // disable/shut down plugin
            Bukkit.getPluginManager().disablePlugin(plugin);
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
