package dev.spaceseries.spacechat;

import org.bstats.bukkit.Metrics;

public class MetricsHandler {

    private final int pluginId = 7508;

    public MetricsHandler() {
        init();
    }

    private void init() {
        // init metrics
        new Metrics(SpaceChat.getInstance(), pluginId);
    }
}
