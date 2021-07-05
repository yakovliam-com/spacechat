package dev.spaceseries.spacechat.config;

import dev.spaceseries.spaceapi.config.adapter.BukkitConfigAdapter;
import dev.spaceseries.spaceapi.config.generic.KeyedConfiguration;
import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;

public final class SpaceChatConfig extends KeyedConfiguration {

    private final ConfigurationAdapter adapter;

    public SpaceChatConfig(BukkitConfigAdapter adapter) {
        super(adapter, SpaceChatConfigKeys.getKeys());

        this.adapter = adapter;

        init();
    }

    @Override
    protected void load(boolean initial) {
        super.load(initial);
    }

    @Override
    public void reload() {
        super.reload();
    }

    public ConfigurationAdapter getAdapter() {
        return adapter;
    }
}
