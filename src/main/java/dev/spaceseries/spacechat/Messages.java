package dev.spaceseries.spacechat;

import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spacechat.api.message.Message;

public class Messages {

    /**
     * Instance of this class
     */
    private static Messages instance;

    public Messages(SpaceChat plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets instance of messages class
     * <p>(Singleton)</p>
     *
     * @return messages
     */
    public static Messages getInstance(SpaceChat context) {
        if (instance == null)
            instance = new Messages(context);
        return instance;
    }


    /**
     * Renews the messages
     */
    public static void renew() {
        instance = null;
    }

    private final SpaceChat plugin;

    /* General */

    // help
    public Message generalHelp = Message.fromConfigurationSection(getLangConfiguration(), "general.help")
            .build();

    /* Reload */

    // success
    public Message reloadSuccess = Message.fromConfigurationSection(getLangConfiguration(), "reload.success")
            .build();

    // failure
    public Message reloadFailure = Message.fromConfigurationSection(getLangConfiguration(), "reload.failure")
            .build();

    /**
     * Broadcast
     */

    // args
    public Message broadcastArgs = Message.fromConfigurationSection(getLangConfiguration(), "broadcast.args")
            .build();

    // wrapper
    public Message broadcastWrapper = Message.fromConfigurationSection(getLangConfiguration(), "broadcast.wrapper")
            .build();

    /**
     * Channel
     */

    // join
    public Message channelJoin = Message.fromConfigurationSection(getLangConfiguration(), "channel.join")
            .build();

    // leave
    public Message channelLeave = Message.fromConfigurationSection(getLangConfiguration(), "channel.leave")
            .build();

    // listen
    public Message channelListen = Message.fromConfigurationSection(getLangConfiguration(), "channel.listen")
            .build();

    // mute
    public Message channelMute = Message.fromConfigurationSection(getLangConfiguration(), "channel.mute")
            .build();

    // invalid
    public Message channelInvalid = Message.fromConfigurationSection(getLangConfiguration(), "channel.invalid")
            .build();

    /**
     * Ignore
     */

    // player not found
    public Message playerNotFound = Message.fromConfigurationSection(getLangConfiguration(), "ignore.player-not-found")
            .build();

    /**
     * Gets the lang configuration from the main class
     *
     * @return The lang configuration
     */
    private ConfigurationAdapter getLangConfiguration() {
        return plugin.getLangConfig().getAdapter();
    }
}

