package com.yakovliam.spacechat.loader;

import com.yakovliam.spaceapi.config.impl.Configuration;
import com.yakovliam.spacechat.SpaceChat;
import com.yakovliam.spacechat.builder.format.FormatBuilder;
import com.yakovliam.spacechat.manager.FormatManager;

public class FormatLoader {

    /**
     * Initializes
     */
    public FormatLoader() {
        // no-op
    }

    /**
     * Loads all formats
     */
    public void loadAllFormats() {
        loadChatFormats();
    }

    /**
     * Loads chat formats
     */
    private void loadChatFormats() {
        // get chat format manager
        FormatManager chatFormatManager = SpaceChat.getInstance().getChatFormatManager();

        // get configuration section for chat formats
        Configuration configuration = SpaceChat.getInstance().getFormatsConfig().getConfig().getSection("chat");

        // loop through keys
        for (String handle : configuration.getKeys()) {
            // add to manager
            chatFormatManager.add(handle, new FormatBuilder().build(configuration.getSection(handle)));
        }
    }
}
