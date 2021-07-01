package dev.spaceseries.spacechat.config;

import dev.spaceseries.spaceapi.config.generic.KeyedConfiguration;
import dev.spaceseries.spaceapi.config.generic.key.ConfigKey;
import dev.spaceseries.spaceapi.config.generic.key.ConfigKeyFactory;
import dev.spaceseries.spaceapi.config.generic.key.SimpleConfigKey;

import java.util.List;

public class SpaceChatConfigKeys {

    public static ConfigKey<String> STORAGE_USE = ConfigKeyFactory.key(c -> c.getString("storage.use", "yaml"));
    public static ConfigKey<String> STORAGE_MYSQL_ADDRESS = ConfigKeyFactory.key(c -> c.getString("storage.mysql.address", null));
    public static ConfigKey<Integer> STORAGE_MYSQL_PORT = ConfigKeyFactory.key(c -> c.getInteger("storage.mysql.port", 3306));
    public static ConfigKey<String> STORAGE_MYSQL_USERNAME = ConfigKeyFactory.key(c -> c.getString("storage.mysql.username", null));
    public static ConfigKey<String> STORAGE_MYSQL_PASSWORD = ConfigKeyFactory.key(c -> c.getString("storage.mysql.password", null));
    public static ConfigKey<String> STORAGE_MYSQL_DATABASE = ConfigKeyFactory.key(c -> c.getString("storage.mysql.database", null));
    public static ConfigKey<Boolean> STORAGE_MYSQL_USE_SSL = ConfigKeyFactory.key(c -> c.getBoolean("storage.mysql.use-ssl", false));
    public static ConfigKey<Boolean> STORAGE_MYSQL_VERIFY_SERVER_CERTIFICATE = ConfigKeyFactory.key(c -> c.getBoolean("storage.mysql.verify-server-certificate", false));
    public static ConfigKey<String> STORAGE_MYSQL_TABLES_CHAT_LOGS = ConfigKeyFactory.key(c -> c.getString("storage.mysql.tables.chat-logs", null));
    public static ConfigKey<String> STORAGE_MYSQL_TABLES_USERS = ConfigKeyFactory.key(c -> c.getString("storage.mysql.tables.users", null));
    public static ConfigKey<String> STORAGE_MYSQL_TABLES_SUBSCRIBED_CHANNELS = ConfigKeyFactory.key(c -> c.getString("storage.mysql.tables.subscribed-channels", null));

    public static ConfigKey<String> STORAGE_SQLITE_DATABASE = ConfigKeyFactory.key(c -> c.getString("storage.sqlite.database", null));
    public static ConfigKey<String> STORAGE_SQLITE_TABLES_CHAT_LOGS = ConfigKeyFactory.key(c -> c.getString("storage.sqlite.tables.chat-logs", null));
    public static ConfigKey<String> STORAGE_SQLITE_TABLES_USERS = ConfigKeyFactory.key(c -> c.getString("storage.sqlite.tables.users", null));
    public static ConfigKey<String> STORAGE_SQLITE_TABLES_SUBSCRIBED_CHANNELS = ConfigKeyFactory.key(c -> c.getString("storage.sqlite.tables.subscribed-channels", null));

    public static ConfigKey<Boolean> REDIS_ENABLED = ConfigKeyFactory.key(c -> c.getBoolean("redis.enabled", false));
    public static ConfigKey<String> REDIS_URL = ConfigKeyFactory.key(c -> c.getString("redis.url", null));
    public static ConfigKey<String> REDIS_CHAT_CHANNEL = ConfigKeyFactory.key(c -> c.getString("redis.chat-channel", null));
    public static ConfigKey<String> REDIS_BROADCAST_CHANNEL = ConfigKeyFactory.key(c -> c.getString("redis.broadcast-channel", null));
    public static ConfigKey<String> REDIS_SERVER_IDENTIFIER = ConfigKeyFactory.key(c -> c.getString("redis.server.identifier", null));
    public static ConfigKey<String> REDIS_SERVER_DISPLAYNAME = ConfigKeyFactory.key(c -> c.getString("redis.server.displayName", null));
    public static ConfigKey<String> REDIS_PLAYER_SUBSCRIBED_CHANNELS_LIST_KEY = ConfigKeyFactory.key(c -> c.getString("redis.player-subscribed-channels-list-key", null));
    public static ConfigKey<String> REDIS_PLAYER_CURRENT_CHANNEL_KEY = ConfigKeyFactory.key(c -> c.getString("redis.player-current-channel-key", null));
    public static ConfigKey<String> REDIS_CHANNELS_SUBSCRIBED_UUIDS_LIST_KEY = ConfigKeyFactory.key(c -> c.getString("redis.channels-subscribed-uuids-list-key", null));

    public static ConfigKey<Boolean> LOGGING_CHAT_LOG_TO_STORAGE = ConfigKeyFactory.key(c -> c.getBoolean("logging.chat.log-to-storage", true));

    public static ConfigKey<String> PERMISSIONS_USE_CHAT_COLORS = ConfigKeyFactory.key(c -> c.getString("permissions.use-chat-colors", null));
    public static ConfigKey<String> PERMISSIONS_USE_ITEM_CHAT = ConfigKeyFactory.key(c -> c.getString("permissions.use-item-chat", null));

    public static ConfigKey<Boolean> BROADCAST_USE_LANG_WRAPPER = ConfigKeyFactory.key(c -> c.getBoolean("broadcast.use-lang-wrapper", false));

    public static ConfigKey<Boolean> ITEM_CHAT_ENABLED = ConfigKeyFactory.key(c -> c.getBoolean("item-chat.enabled", false));
    public static ConfigKey<List<String>> ITEM_CHAT_REPLACE_ALIASES = ConfigKeyFactory.key(c -> c.getStringList("item-chat.replace-aliases", null));
    public static ConfigKey<String> ITEM_CHAT_WITH_CHAT = ConfigKeyFactory.key(c -> c.getString("item-chat.with.chat", null));
    public static ConfigKey<Boolean> ITEM_CHAT_WITH_LORE_USE_CUSTOM = ConfigKeyFactory.key(c -> c.getBoolean("item-chat.with.lore.use-custom", false));
    public static ConfigKey<List<String>> ITEM_CHAT_WITH_LORE_CUSTOM = ConfigKeyFactory.key(c -> c.getStringList("item-chat.with.lore.custom", null));
    public static ConfigKey<Integer> ITEM_CHAT_MAX_PER_MESSAGE = ConfigKeyFactory.key(c -> c.getInteger("item-chat.max-per-message", 2));

    public static ConfigKey<Boolean> USE_RELATIONAL_PLACEHOLDERS = ConfigKeyFactory.key(c -> c.getBoolean("use-relational-placeholders", false));

    public static ConfigKey<Boolean> OWNER_JOIN = ConfigKeyFactory.key(c -> c.getBoolean("owner-join", true));

    private static final List<SimpleConfigKey<?>> KEYS = KeyedConfiguration.initialise(SpaceChatConfigKeys.class);

    public static List<? extends ConfigKey<?>> getKeys() {
        return KEYS;
    }
}
