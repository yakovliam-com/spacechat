package dev.spaceseries.spacechat.storage.impl.yaml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.spaceseries.api.config.obj.Config;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.storage.Storage;

import java.io.File;
import java.util.Date;

public class YamlStorage implements Storage {

    /**
     * The Yaml configuration for logging
     */
    private final YamlStorageConfig config;

    /**
     * The gson instance
     */
    private final Gson gson;

    public YamlStorage() {
        config = new YamlStorageConfig();

        GsonBuilder gsonBuilder = new GsonBuilder();
        // register custom serializer & deserializer for gson
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());

        gson = gsonBuilder.create();
    }

    @Override
    public void log(LogWrapper data) {
        // if chat
        if (data.getLogType() == LogType.CHAT) {
            logChat((LogChatWrap) data);
        }
    }

    /**
     * Logs chat to the Yaml configuration
     *
     * @param data The data
     */
    private void logChat(LogChatWrap data) {
        // object (json) for data
        String json = gson.toJson(data);
        // set new key (date as milliseconds is the key)
        config.set("chat." + data.getAt().getTime(), json);
    }

    private static final class YamlStorageConfig extends Config {

        public YamlStorageConfig() {
            super(SpaceChat.getInstance().getPlugin(), new File(SpaceChat.getInstance().getDataFolder() + File.separator + "storage"), "logs.yml");
        }

    }
}
