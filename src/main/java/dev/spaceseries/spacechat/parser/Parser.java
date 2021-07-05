package dev.spaceseries.spacechat.parser;

import dev.spaceseries.spacechat.SpaceChat;

public abstract class Parser<K, V> {

    protected final SpaceChat plugin;

    public Parser(SpaceChat plugin) {
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
