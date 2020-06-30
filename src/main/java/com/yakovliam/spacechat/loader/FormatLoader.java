package com.yakovliam.spacechat.loader;

import com.yakovliam.spaceapi.config.impl.Configuration;
import com.yakovliam.spacechat.builder.format.FormatBuilder;
import com.yakovliam.spacechat.manager.FormatManager;

public class FormatLoader {

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
