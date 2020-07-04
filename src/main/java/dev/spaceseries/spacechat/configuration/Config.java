package dev.spaceseries.spacechat.configuration;

import dev.spaceseries.spacechat.SpaceChat;

public final class Config extends dev.spaceseries.api.config.obj.Config {

    public Config() {
        super(SpaceChat.getInstance().getPlugin(), "config.yml");
    }
}
