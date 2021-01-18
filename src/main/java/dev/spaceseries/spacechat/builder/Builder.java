package dev.spaceseries.spacechat.builder;

public interface Builder<K, V> {

    /**
     * Builds an V (output) from a K (input)
     */
    V build(K input);
}
