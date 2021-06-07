package dev.spaceseries.spacechat.builder.live;

import dev.spaceseries.spacechat.SpaceChat;

public abstract class LiveChatFormatBuilder {

    protected final SpaceChat plugin;

    public LiveChatFormatBuilder(SpaceChat plugin) {
        this.plugin = plugin;
    }
}
