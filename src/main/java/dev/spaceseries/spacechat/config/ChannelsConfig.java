package dev.spaceseries.spacechat.config;

import dev.spaceseries.spaceapi.config.obj.Config;
import dev.spaceseries.spacechat.SpaceChat;

public class ChannelsConfig extends Config {

    public ChannelsConfig() {
        super(SpaceChat.getInstance().getPlugin(), "channels.yml");
    }
}
