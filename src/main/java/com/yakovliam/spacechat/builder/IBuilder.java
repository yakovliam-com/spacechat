package com.yakovliam.spacechat.builder;

public interface IBuilder<K, V> {

    /**
     * Builds an V (output) from a K (input)
     */
    V build(K input);
}
