package dev.spaceseries.spacechat.parser;

public interface Parser<K, V> {

    /**
     * Parse
     *
     * @param k k
     * @return v
     */
    V parse(K k);
}
