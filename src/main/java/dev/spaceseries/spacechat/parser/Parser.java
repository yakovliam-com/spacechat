package dev.spaceseries.spacechat.parser;

import dev.spaceseries.spacechat.SpaceChatPlugin;

public abstract class Parser<K, V> {

    protected final SpaceChatPlugin plugin;

    public Parser(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Parse
     *
     * @param k k
     * @return v
     */
    public abstract V parse(K k);
}
