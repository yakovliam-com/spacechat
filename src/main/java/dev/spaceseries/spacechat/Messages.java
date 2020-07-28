package dev.spaceseries.spacechat;

import dev.spaceseries.api.config.impl.Configuration;
import dev.spaceseries.api.text.Message;

public class Messages {

    /* General */

    // help
    public static final Message GENERAL_HELP = Message.fromConfigurationSection(getLangConfiguration().getSection("general.help"), "general.help")
            .build();

    /* Reload */

    // success
    public static final Message RELOAD_SUCCESS = Message.fromConfigurationSection(getLangConfiguration().getSection("reload.success"), "reload.success")
            .build();

    // failure
    public static final Message RELOAD_FAILURE = Message.fromConfigurationSection(getLangConfiguration().getSection("reload.failure"), "reload.failure")
            .build();

    /**
     * Gets the lang configuration from the main class
     *
     * @return The lang configuration
     */
    private static Configuration getLangConfiguration() {
        return SpaceChat.getInstance().getLangConfig().getConfig();
    }
}

