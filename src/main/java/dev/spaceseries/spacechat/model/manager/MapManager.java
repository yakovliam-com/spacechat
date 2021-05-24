package dev.spaceseries.spacechat.model.manager;

import java.util.*;

public abstract class MapManager<K, V> implements Manager {

    /**
     * Map
     */
    private Map<K, V> map;

    /**
     * Construct map manager
     */
    public MapManager() {
        this.map = new HashMap<>();
    }

    /**
     * Construct map manager
     * <p>
     * Creates a clone of the map and initializes the map manager with the clone
     *
     * @param def default, initialized
     */
    public MapManager(Map<K, V> def) {
        this.map.putAll(def);
    }

    /**
     * Returns the map
     *
     * @return map
     */
    public Map<K, V> getAll() {
        return this.map;
    }

    /**
     * Returns gotten value
     *
     * @param k key
     * @return gotten value
     */
    public V get(K k) {
        return map.get(k);
    }

    /**
     * Returns gotten value
     *
     * @param k   key
     * @param def default
     * @return gotten value
     */
    public V get(K k, V def) {
        return map.getOrDefault(k, def);
    }

    /**
     * Adds to the map
     *
     * @param k k
     * @param v v
     */
    public void add(K k, V v) {
        this.map.put(k, v);
    }

    /**
     * Clears the map
     */
    public void clear() {
        this.map.clear();
    }
}
