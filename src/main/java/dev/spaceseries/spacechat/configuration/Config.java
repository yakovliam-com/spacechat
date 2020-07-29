package dev.spaceseries.spacechat.configuration;

import dev.spaceseries.api.config.impl.Configuration;
import dev.spaceseries.api.config.keys.ConfigKey;
import dev.spaceseries.api.config.keys.ConfigKeyTypes;
import dev.spaceseries.spacechat.SpaceChat;

public final class Config extends dev.spaceseries.api.config.obj.Config {

    public Config() {
        super(SpaceChat.getInstance().getPlugin(), "config.yml");
    }

    public static final ConfigKey<String> USE = ConfigKeyTypes.stringKey("use", "yaml");
    public static final ConfigKey<String> YAML_TABLES_LOGS = ConfigKeyTypes.stringKey("yaml.tables.logs", null);

    public static final ConfigKey<Boolean> LOGGING_CHAT_LOG_TO_CONSOLE = ConfigKeyTypes.booleanKey("logging.chat.log-to-console", true);
    public static final ConfigKey<Boolean> LOGGING_CHAT_LOG_TO_STORAGE = ConfigKeyTypes.booleanKey("logging.chat.log-to-storage", true);

    public static Configuration get() {
        return SpaceChat.getInstance().getSpaceChatConfig().getConfig();
    }
}
