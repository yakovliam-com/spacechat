package com.yakovliam.spacechat.manager;

import com.yakovliam.spacechat.model.Format;

import java.util.HashMap;
import java.util.Map;

public class FormatManager implements IManager<String, Format> {

    /**
     * The format map
     */
    private final Map<String, Format> formatMap;

    /**
     * Initializes
     */
    public FormatManager() {
        formatMap = new HashMap<>();
    }

    /**
     * Gets all
     */
    @Override
    public Map<String, Format> getAll() {
        return formatMap;
    }

    /**
     * Gets a format by handle
     *
     * @param handle The handle
     * @return The format
     */
    @Override
    public Format get(String handle) {
        return formatMap.getOrDefault(handle, null);
    }

    /**
     * Gets a format by handle
     *
     * @param handle The handle
     * @param def The default value
     * @return The format
     */
    @Override
    public Format get(String handle, Format def) {
        return formatMap.getOrDefault(handle, def);
    }


    /**
     * Adds a format
     *
     * @param handle The handle
     * @param format The format
     */
    @Override
    public void add(String handle, Format format) {
        formatMap.put(handle, format);
    }

    /**
     * Clears the map
     */
    public void clear() {
        formatMap.clear();
    }
}
