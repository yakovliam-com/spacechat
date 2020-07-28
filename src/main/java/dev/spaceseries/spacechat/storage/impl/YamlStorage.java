package dev.spaceseries.spacechat.storage.impl;

import dev.spaceseries.api.config.obj.Config;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.storage.IStorage;

import java.io.File;

public class YamlStorage implements IStorage {

    /**
     * The Yaml configuration for logging
     */
    private YamlStorageConfig config;

    public YamlStorage() {
        config = new YamlStorageConfig();
    }

    @Override
    public void log(LogWrapper data) {
        //todo
    }

    private final class YamlStorageConfig extends Config {

        public YamlStorageConfig() {
            super(SpaceChat.getInstance().getPlugin(), new File("storage"), "logs.yml");
        }
    }
}
