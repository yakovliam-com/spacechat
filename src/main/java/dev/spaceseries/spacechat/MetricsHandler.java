package dev.spaceseries.spacechat;

import org.bstats.bukkit.Metrics;

public class MetricsHandler {

    public MetricsHandler() {
        init();
    }

    private void init() {
        // init metrics
        int pluginId = 7508;
        new Metrics(SpaceChat.getInstance(), pluginId);
    }
}
