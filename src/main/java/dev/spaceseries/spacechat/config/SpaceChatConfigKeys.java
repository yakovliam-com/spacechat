package dev.spaceseries.spacechat.config;

import com.google.common.collect.ImmutableMap;
import dev.spaceseries.spacechat.api.config.generic.KeyedConfiguration;
import dev.spaceseries.spacechat.api.config.generic.key.ConfigKey;
import dev.spaceseries.spacechat.api.config.generic.key.SimpleConfigKey;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.StorageCredentials;

import java.util.List;
import java.util.Map;

import static dev.spaceseries.spacechat.api.config.generic.key.ConfigKeyFactory.key;
import static dev.spaceseries.spacechat.api.config.generic.key.ConfigKeyFactory.notReloadable;

public class SpaceChatConfigKeys {

    public static ConfigKey<String> STORAGE_USE = key(c -> c.getString("storage.use", null));
    public static ConfigKey<String> STORAGE_MYSQL_TABLES_CHAT_LOGS = key(c -> c.getString("storage.mysql.tables.chat-logs", null));
    public static ConfigKey<String> STORAGE_MYSQL_TABLES_USERS = key(c -> c.getString("storage.mysql.tables.users", null));
    public static ConfigKey<String> STORAGE_MYSQL_TABLES_SUBSCRIBED_CHANNELS = key(c -> c.getString("storage.mysql.tables.subscribed-channels", null));

    /**
     * The database settings, username, password, etc for use by any database
     */
    public static final ConfigKey<StorageCredentials> DATABASE_VALUES = notReloadable(key(c -> {
        int maxPoolSize = c.getInteger("storage.mysql.pool-settings.maximum-pool-size", c.getInteger("data.pool-size", 10));
        int minIdle = c.getInteger("storage.mysql.pool-settings.minimum-idle", maxPoolSize);
        int maxLifetime = c.getInteger("storage.mysql.pool-settings.maximum-lifetime", 1800000);
        int keepAliveTime = c.getInteger("storage.mysql.pool-settings.keepalive-time", 0);
        int connectionTimeout = c.getInteger("storage.mysql.pool-settings.connection-timeout", 5000);
        Map<String, String> props = ImmutableMap.copyOf(c.getStringMap("storage.mysql.pool-settings.properties", ImmutableMap.of()));

        return new StorageCredentials(
                c.getString("storage.mysql.address", null),
                c.getString("storage.mysql.database", null),
                c.getString("storage.mysql.username", null),
                c.getString("storage.mysql.password", null),
                maxPoolSize, minIdle, maxLifetime, keepAliveTime, connectionTimeout, props
        );
    }));

    public static ConfigKey<Boolean> REDIS_ENABLED = key(c -> c.getBoolean("redis.enabled", false));
    public static ConfigKey<String> REDIS_URL = key(c -> c.getString("redis.url", null));
    public static ConfigKey<String> REDIS_CHAT_CHANNEL = key(c -> c.getString("redis.chat-channel", null));
    public static ConfigKey<String> REDIS_BROADCAST_CHANNEL = key(c -> c.getString("redis.broadcast-channel", null));
    public static ConfigKey<String> REDIS_SERVER_IDENTIFIER = key(c -> c.getString("redis.server.identifier", null));
    public static ConfigKey<String> REDIS_SERVER_DISPLAYNAME = key(c -> c.getString("redis.server.displayName", null));
    public static ConfigKey<String> REDIS_PLAYER_SUBSCRIBED_CHANNELS_LIST_KEY = key(c -> c.getString("redis.player-subscribed-channels-list-key", null));
    public static ConfigKey<String> REDIS_PLAYER_CURRENT_CHANNEL_KEY = key(c -> c.getString("redis.player-current-channel-key", null));
    public static ConfigKey<String> REDIS_CHANNELS_SUBSCRIBED_UUIDS_LIST_KEY = key(c -> c.getString("redis.channels-subscribed-uuids-list-key", null));

    public static ConfigKey<Boolean> LOGGING_CHAT_LOG_TO_STORAGE = key(c -> c.getBoolean("logging.chat.log-to-storage", true));

    public static ConfigKey<String> PERMISSIONS_USE_CHAT_COLORS = key(c -> c.getString("permissions.use-chat-colors", null));
    public static ConfigKey<String> PERMISSIONS_USE_ITEM_CHAT = key(c -> c.getString("permissions.use-item-chat", null));
    public static ConfigKey<String> PERMISSIONS_USE_CHAT_LINKS = key(c -> c.getString("permissions.use-chat-links", null));

    public static ConfigKey<Boolean> BROADCAST_USE_LANG_WRAPPER = key(c -> c.getBoolean("broadcast.use-lang-wrapper", false));

    public static ConfigKey<Boolean> ITEM_CHAT_ENABLED = key(c -> c.getBoolean("item-chat.enabled", false));
    public static ConfigKey<List<String>> ITEM_CHAT_REPLACE_ALIASES = key(c -> c.getStringList("item-chat.replace-aliases", null));
    public static ConfigKey<String> ITEM_CHAT_WITH_CHAT = key(c -> c.getString("item-chat.with.chat", null));
    public static ConfigKey<Boolean> ITEM_CHAT_WITH_LORE_USE_CUSTOM = key(c -> c.getBoolean("item-chat.with.lore.use-custom", false));
    public static ConfigKey<List<String>> ITEM_CHAT_WITH_LORE_CUSTOM = key(c -> c.getStringList("item-chat.with.lore.custom", null));
    public static ConfigKey<Integer> ITEM_CHAT_MAX_PER_MESSAGE = key(c -> c.getInteger("item-chat.max-per-message", 2));

    public static ConfigKey<Boolean> USE_RELATIONAL_PLACEHOLDERS = key(c -> c.getBoolean("use-relational-placeholders", false));

    private static final List<SimpleConfigKey<?>> KEYS = KeyedConfiguration.initialise(SpaceChatConfigKeys.class);

    public static List<? extends ConfigKey<?>> getKeys() {
        return KEYS;
    }
}
