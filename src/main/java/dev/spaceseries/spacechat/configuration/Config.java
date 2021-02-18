package dev.spaceseries.spacechat.configuration;

import dev.spaceseries.spaceapi.config.impl.Configuration;
import dev.spaceseries.spaceapi.config.keys.ConfigKey;
import dev.spaceseries.spaceapi.config.keys.ConfigKeyTypes;
import dev.spaceseries.spacechat.SpaceChat;

public final class Config extends dev.spaceseries.spaceapi.config.obj.Config {

    public Config() {
        super(SpaceChat.getInstance().getPlugin(), "config.yml");
    }

    public static ConfigKey<String> USE = ConfigKeyTypes.stringKey("use", "yaml");

    public static ConfigKey<String> MYSQL_ADDRESS = ConfigKeyTypes.stringKey("mysql.address", null);
    public static ConfigKey<Integer> MYSQL_PORT = ConfigKeyTypes.integerKey("mysql.port", 3306);
    public static ConfigKey<String> MYSQL_USERNAME = ConfigKeyTypes.stringKey("mysql.username", null);
    public static ConfigKey<String> MYSQL_PASSWORD = ConfigKeyTypes.stringKey("mysql.password", null);
    public static ConfigKey<String> MYSQL_DATABASE = ConfigKeyTypes.stringKey("mysql.database", null);
    public static ConfigKey<Boolean> MYSQL_USE_SSL = ConfigKeyTypes.booleanKey("mysql.use-ssl", false);
    public static ConfigKey<Boolean> MYSQL_VERIFY_SERVER_CERTIFICATE = ConfigKeyTypes.booleanKey("mysql.verify-server-certificate", false);

    public static ConfigKey<String> MYSQL_TABLES_CHAT_LOGS = ConfigKeyTypes.stringKey("mysql.tables.chat-logs", null);

    public static ConfigKey<Boolean> LOGGING_CHAT_LOG_TO_STORAGE = ConfigKeyTypes.booleanKey("logging.chat.log-to-storage", true);

    public static ConfigKey<String> PERMISSIONS_USE_CHAT_COLORS = ConfigKeyTypes.stringKey("permissions.use-chat-colors", null);

    public static Configuration get() {
        return SpaceChat.getInstance().getSpaceChatConfig().getConfig();
    }
}
