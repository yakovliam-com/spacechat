package dev.spaceseries.spacechat.config;

import dev.spaceseries.spaceapi.config.keys.ConfigKey;
import dev.spaceseries.spaceapi.config.keys.ConfigKeyTypes;
import dev.spaceseries.spacechat.SpaceChat;

import java.util.ArrayList;
import java.util.List;

public final class Config extends dev.spaceseries.spaceapi.config.obj.Config {

    public Config(SpaceChat plugin) {
        super(plugin.getPlugin(), "config.yml");
    }

    public static ConfigKey<String> STORAGE_USE = ConfigKeyTypes.stringKey("storage.use", "yaml");

    public static ConfigKey<String> STORAGE_MYSQL_ADDRESS = ConfigKeyTypes.stringKey("storage.mysql.address", null);
    public static ConfigKey<Integer> STORAGE_MYSQL_PORT = ConfigKeyTypes.integerKey("storage.mysql.port", 3306);
    public static ConfigKey<String> STORAGE_MYSQL_USERNAME = ConfigKeyTypes.stringKey("storage.mysql.username", null);
    public static ConfigKey<String> STORAGE_MYSQL_PASSWORD = ConfigKeyTypes.stringKey("storage.mysql.password", null);
    public static ConfigKey<String> STORAGE_MYSQL_DATABASE = ConfigKeyTypes.stringKey("storage.mysql.database", null);
    public static ConfigKey<Boolean> STORAGE_MYSQL_USE_SSL = ConfigKeyTypes.booleanKey("storage.mysql.use-ssl", false);
    public static ConfigKey<Boolean> STORAGE_MYSQL_VERIFY_SERVER_CERTIFICATE = ConfigKeyTypes.booleanKey("storage.mysql.verify-server-certificate", false);
    public static ConfigKey<String> STORAGE_MYSQL_TABLES_CHAT_LOGS = ConfigKeyTypes.stringKey("storage.mysql.tables.chat-logs", null);
    public static ConfigKey<String> STORAGE_MYSQL_TABLES_USERS = ConfigKeyTypes.stringKey("storage.mysql.tables.users", null);
    public static ConfigKey<String> STORAGE_MYSQL_TABLES_SUBSCRIBED_CHANNELS = ConfigKeyTypes.stringKey("storage.mysql.tables.subscribed-channels", null);

    public static ConfigKey<String> STORAGE_SQLITE_DATABASE = ConfigKeyTypes.stringKey("storage.sqlite.database", null);
    public static ConfigKey<String> STORAGE_SQLITE_TABLES_CHAT_LOGS = ConfigKeyTypes.stringKey("storage.sqlite.tables.chat-logs", null);
    public static ConfigKey<String> STORAGE_SQLITE_TABLES_USERS = ConfigKeyTypes.stringKey("storage.sqlite.tables.users", null);
    public static ConfigKey<String> STORAGE_SQLITE_TABLES_SUBSCRIBED_CHANNELS = ConfigKeyTypes.stringKey("storage.sqlite.tables.subscribed-channels", null);

    public static ConfigKey<Boolean> REDIS_ENABLED = ConfigKeyTypes.booleanKey("redis.enabled", false);
    public static ConfigKey<String> REDIS_URL = ConfigKeyTypes.stringKey("redis.url", null);
    public static ConfigKey<String> REDIS_CHAT_CHANNEL = ConfigKeyTypes.stringKey("redis.chat-channel", null);
    public static ConfigKey<String> REDIS_BROADCAST_CHANNEL = ConfigKeyTypes.stringKey("redis.broadcast-channel", null);
    public static ConfigKey<String> REDIS_SERVER_IDENTIFIER = ConfigKeyTypes.stringKey("redis.server.identifier", null);
    public static ConfigKey<String> REDIS_SERVER_DISPLAYNAME = ConfigKeyTypes.stringKey("redis.server.displayName", null);
    public static ConfigKey<String> REDIS_PLAYER_SUBSCRIBED_CHANNELS_LIST_KEY = ConfigKeyTypes.stringKey("redis.player-subscribed-channels-list-key", null);
    public static ConfigKey<String> REDIS_PLAYER_CURRENT_CHANNEL_KEY = ConfigKeyTypes.stringKey("redis.player-current-channel-key", null);
    public static ConfigKey<String> REDIS_CHANNELS_SUBSCRIBED_UUIDS_LIST_KEY = ConfigKeyTypes.stringKey("redis.channels-subscribed-uuids-list-key", null);

    public static ConfigKey<Boolean> LOGGING_CHAT_LOG_TO_STORAGE = ConfigKeyTypes.booleanKey("logging.chat.log-to-storage", true);

    public static ConfigKey<String> PERMISSIONS_USE_CHAT_COLORS = ConfigKeyTypes.stringKey("permissions.use-chat-colors", null);
    public static ConfigKey<String> PERMISSIONS_USE_ITEM_CHAT = ConfigKeyTypes.stringKey("permissions.use-item-chat", null);

    public static ConfigKey<Boolean> BROADCAST_USE_LANG_WRAPPER = ConfigKeyTypes.booleanKey("broadcast.use-lang-wrapper", false);

    public static ConfigKey<Boolean> ITEM_CHAT_ENABLED = ConfigKeyTypes.booleanKey("item-chat.enabled", false);
    public static ConfigKey<List<String>> ITEM_CHAT_REPLACE_ALIASES = ConfigKeyTypes.stringListKey("item-chat.replace-aliases", new ArrayList<>());
    public static ConfigKey<String> ITEM_CHAT_WITH_CHAT = ConfigKeyTypes.stringKey("item-chat.with.chat", null);
    public static ConfigKey<Boolean> ITEM_CHAT_WITH_LORE_USE_CUSTOM = ConfigKeyTypes.booleanKey("item-chat.with.lore.use-custom", false);
    public static ConfigKey<List<String>> ITEM_CHAT_WITH_LORE_CUSTOM = ConfigKeyTypes.stringListKey("item-chat.with.lore.custom", new ArrayList<>());
    public static ConfigKey<Integer> ITEM_CHAT_MAX_PER_MESSAGE = ConfigKeyTypes.integerKey("item-chat.max-per-message", 2);

    public static ConfigKey<Boolean> USE_RELATIONAL_PLACEHOLDERS = ConfigKeyTypes.booleanKey("use-relational-placeholders", false);

    public static ConfigKey<Boolean> OWNER_JOIN = ConfigKeyTypes.booleanKey("owner-join", true);
}
