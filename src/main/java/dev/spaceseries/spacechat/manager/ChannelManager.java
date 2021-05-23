package dev.spaceseries.spacechat.manager;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.loader.ChannelLoader;
import dev.spaceseries.spacechat.model.ChannelType;
import dev.spaceseries.spacechat.model.Channel;

import java.util.Locale;

public class ChannelManager extends MapManager<String, Channel> {

    /**
     * The format loader
     */
    private final ChannelLoader channelLoader;

    /**
     * Initializes
     */
    public ChannelManager() {
        // create format manager
        this.channelLoader = new ChannelLoader(SpaceChat.getInstance()
                .getChannelsConfig()
                .getConfig()
                .getSection(
                        ChannelType.NORMAL.getSectionKey().toLowerCase(Locale.ROOT)
                )
        );

        // load
        this.loadFormats();
    }

    /**
     * Loads formats
     */
    public void loadFormats() {
        channelLoader.load(this);
    }
}
