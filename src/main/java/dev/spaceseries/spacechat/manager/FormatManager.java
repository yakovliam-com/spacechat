package dev.spaceseries.spacechat.manager;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.loader.FormatLoader;
import dev.spaceseries.spacechat.loader.FormatType;
import dev.spaceseries.spacechat.model.Format;

import java.util.HashMap;
import java.util.Map;

public class FormatManager implements IManager<String, Format> {

    /**
     * The format map
     */
    private final Map<String, Format> formatMap;

    /**
     * The format loader
     */
    private final FormatLoader formatLoader;

    /**
     * Initializes
     */
    public FormatManager(FormatType type) {
        this.formatMap = new HashMap<>();

        // create format manager
        this.formatLoader = new FormatLoader(SpaceChat.getInstance()
                .getFormatsConfig()
                .getConfig()
                .getSection(
                        type.getSectionKey().toLowerCase()
                )
        );

        // load
        this.loadFormats();
    }

    /**
     * Loads formats
     */
    public void loadFormats() {
        formatLoader.load(this);
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
     * @param def    The default value
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
