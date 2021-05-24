package dev.spaceseries.spacechat.loader;

import dev.spaceseries.spaceapi.config.impl.Configuration;
import dev.spaceseries.spacechat.builder.chatformat.ChatFormatBuilder;
import dev.spaceseries.spacechat.model.formatting.ChatFormat;

public class ChatFormatLoader extends FormatLoader<ChatFormat> {

    /**
     * Initializes
     */
    public ChatFormatLoader(Configuration formatsSection) {
        super(formatsSection);
    }

    /**
     * Loads chat formats
     */
    @Override
    public void load(FormatManager<ChatFormat> formatManager) {
        // loop through section keys
        for (String handle : formatsSection.getKeys()) {
            // add to manager
            formatManager.add(handle, new ChatFormatBuilder().build(formatsSection.getSection(handle)));
        }
    }
}
