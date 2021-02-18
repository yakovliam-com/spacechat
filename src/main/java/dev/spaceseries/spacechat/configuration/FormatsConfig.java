package dev.spaceseries.spacechat.configuration;

import dev.spaceseries.spaceapi.config.obj.Config;
import dev.spaceseries.spacechat.SpaceChat;

public final class FormatsConfig extends Config {

    public FormatsConfig() {
        super(SpaceChat.getInstance().getPlugin(), "formats.yml");
    }
}
