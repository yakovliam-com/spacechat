package dev.spaceseries.spacechat.loader;


import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.manager.MapManager;

public abstract class FormatManager<V> extends MapManager<String, V> {

    protected SpaceChatPlugin plugin;

    public FormatManager(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }
}
