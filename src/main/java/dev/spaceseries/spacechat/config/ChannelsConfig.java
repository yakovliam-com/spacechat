package dev.spaceseries.spacechat.config;

import dev.spaceseries.spaceapi.config.obj.Config;
import dev.spaceseries.spacechat.SpaceChat;

public class ChannelsConfig extends Config {

    public ChannelsConfig(SpaceChat plugin) {
        super(plugin.getPlugin(), "channels.yml");
    }
}
