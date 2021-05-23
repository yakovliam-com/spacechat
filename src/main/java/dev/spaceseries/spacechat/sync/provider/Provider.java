package dev.spaceseries.spacechat.sync.provider;

public interface Provider<T> {

    /**
     * Provides something
     *
     * @return provided
     */
    T provide();
}
