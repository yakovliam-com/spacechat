package dev.spaceseries.spacechat.builder.live;

import dev.spaceseries.spacechat.SpaceChatPlugin;

public abstract class LiveChatFormatBuilder {

    protected final SpaceChatPlugin plugin;

    public LiveChatFormatBuilder(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }
}
