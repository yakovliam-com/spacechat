package dev.spaceseries.spacechat.configuration;

import dev.spaceseries.api.config.impl.Configuration;
import dev.spaceseries.api.config.keys.ConfigKey;
import dev.spaceseries.api.config.keys.ConfigKeyTypes;
import dev.spaceseries.spacechat.SpaceChat;

public final class Config extends dev.spaceseries.api.config.obj.Config {

    public Config() {
        super(SpaceChat.getInstance().getPlugin(), "config.yml");
    }

    public static ConfigKey<String> USE = ConfigKeyTypes.stringKey("use", "yaml");

    public static ConfigKey<String> MYSQL_ADDRESS = ConfigKeyTypes.stringKey("mysql.address", null);
    public static ConfigKey<Integer> MYSQL_PORT = ConfigKeyTypes.integerKey("mysql.port", 3306);
    public static ConfigKey<String> MYSQL_USERNAME = ConfigKeyTypes.stringKey("mysql.username", null);
    public static ConfigKey<String> MYSQL_PASSWORD = ConfigKeyTypes.stringKey("mysql.password", null);
    public static ConfigKey<String> MYSQL_DATABASE = ConfigKeyTypes.stringKey("mysql.database", null);
    public static ConfigKey<String> MYSQL_TABLES_CHAT_LOGS = ConfigKeyTypes.stringKey("mysql.tables.chat-logs", null);

    public static ConfigKey<Boolean> LOGGING_CHAT_LOG_TO_CONSOLE = ConfigKeyTypes.booleanKey("logging.chat.log-to-console", true);
    public static ConfigKey<Boolean> LOGGING_CHAT_LOG_TO_STORAGE = ConfigKeyTypes.booleanKey("logging.chat.log-to-storage", true);

    public static Configuration get() {
        return SpaceChat.getInstance().getSpaceChatConfig().getConfig();
    }
}
