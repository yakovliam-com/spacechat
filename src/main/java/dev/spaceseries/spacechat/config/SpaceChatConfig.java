package dev.spaceseries.spacechat.config;

import dev.spaceseries.spacechat.api.config.adapter.BukkitConfigAdapter;
import dev.spaceseries.spacechat.api.config.generic.KeyedConfiguration;
import dev.spaceseries.spacechat.api.config.generic.adapter.ConfigurationAdapter;

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
