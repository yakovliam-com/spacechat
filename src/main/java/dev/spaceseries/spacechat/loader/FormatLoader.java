package dev.spaceseries.spacechat.loader;

import dev.spaceseries.spaceapi.config.impl.Configuration;
import dev.spaceseries.spacechat.builder.format.FormatBuilder;
import dev.spaceseries.spacechat.manager.FormatManager;

public class FormatLoader implements Loader {

    /**
     * The format section
     */
    private final Configuration formatSection;

    /**
     * Initializes
     */
    public FormatLoader(Configuration formatSection) {
        this.formatSection = formatSection;
    }

    /**
     * Loads chat formats
     */
    public void load(FormatManager formatManager) {
        // loop through section keys
        for (String handle : formatSection.getKeys()) {
            // add to manager
            formatManager.add(handle, new FormatBuilder().build(formatSection.getSection(handle)));
        }
    }
}
