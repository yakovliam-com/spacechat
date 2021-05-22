package dev.spaceseries.spacechat.loader;

import dev.spaceseries.spaceapi.config.impl.Configuration;
import dev.spaceseries.spacechat.builder.chatformat.ChatFormatBuilder;
import dev.spaceseries.spacechat.model.ChatFormat;

public class ChatFormatLoader extends FormatLoader<ChatFormat> {

    /**
     * Initializes
     */
    public ChatFormatLoader(Configuration formatSection) {
        super(formatSection);
    }

    /**
     * Loads chat formats
     */
    @Override
    public void load(FormatManager<ChatFormat> formatManager) {
        // loop through section keys
        for (String handle : formatSection.getKeys()) {
            // add to manager
            formatManager.add(handle, new ChatFormatBuilder().build(formatSection.getSection(handle)));
        }
    }
}
