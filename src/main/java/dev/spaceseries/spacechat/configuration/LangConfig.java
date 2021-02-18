package dev.spaceseries.spacechat.configuration;

import dev.spaceseries.spaceapi.config.obj.Config;
import dev.spaceseries.spacechat.SpaceChat;

public final class LangConfig extends Config {

    public LangConfig() {
        super(SpaceChat.getInstance().getPlugin(), "lang.yml");
    }
}
