package dev.spaceseries.spacechat.loader;

import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spaceapi.util.Trio;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.builder.chatformat.ChatFormatBuilder;
import dev.spaceseries.spacechat.model.formatting.ChatFormat;

import java.util.ArrayList;

public class ChatFormatLoader extends FormatLoader<ChatFormat> {

    /**
     * Initializes
     */
    public ChatFormatLoader(SpaceChat plugin, String formatsSection) {
        super(plugin, formatsSection);
    }

    /**
     * Loads chat formats
     */
    @Override
    public void load(FormatManager<ChatFormat> formatManager) {
        ConfigurationAdapter adapter = getPlugin().getFormatsConfig().getAdapter();

        // loop through section keys
        for (String handle : adapter.getKeys(formatsSection, new ArrayList<>())) {
            // add to manager
            formatManager.add(handle, new ChatFormatBuilder().build(new Trio<>(formatsSection + "." + handle, handle, adapter)));
        }
    }
}
