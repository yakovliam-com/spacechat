package dev.spaceseries.spacechat.loader;

import dev.spaceseries.spaceapi.config.impl.Configuration;
import dev.spaceseries.spacechat.builder.channel.ChannelBuilder;
import dev.spaceseries.spacechat.model.manager.MapManager;
import dev.spaceseries.spacechat.model.Channel;

public class ChannelLoader implements Loader<MapManager<String, Channel>> {

    /**
     * Section for channels
     */
    private final Configuration channelsSections;

    /**
     * Initializes
     */
    public ChannelLoader(Configuration channelsSections) {
        this.channelsSections = channelsSections;
    }


    /**
     * Load channels
     *
     * @param stringChannelMapManager channel manager
     */
    @Override
    public void load(MapManager<String, Channel> stringChannelMapManager) {
        // loop through section keys
        for (String handle : channelsSections.getKeys()) {
            // add to manager
            stringChannelMapManager.add(handle, new ChannelBuilder().build(channelsSections.getSection(handle)));
        }
    }
}

