package dev.spaceseries.spacechat.loader;


import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.model.manager.MapManager;

public abstract class FormatManager<V> extends MapManager<String, V> {

    protected SpaceChat plugin;

    public FormatManager(SpaceChat plugin) {
        this.plugin = plugin;
    }
}
