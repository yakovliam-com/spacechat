package dev.spaceseries.spacechat.manager;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.loader.FormatLoader;
import dev.spaceseries.spacechat.loader.FormatType;
import dev.spaceseries.spacechat.model.Format;

public class FormatManager extends MapManager<String, Format> {

    /**
     * The format loader
     */
    private final FormatLoader formatLoader;

    /**
     * Initializes
     */
    public FormatManager(FormatType type) {
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
}
