package dev.spaceseries.spacechat.loader;

public interface Loader<T> {

    /**
     * Loads something using a t
     *
     * @param t t
     */
    void load(T t);
}
