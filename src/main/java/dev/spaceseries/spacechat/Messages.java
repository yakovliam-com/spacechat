package dev.spaceseries.spacechat;

import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spacechat.api.message.Message;

public class Messages {

    /**
     * Instance of this class
     */
    private static Messages instance;

    /**
     * Renews the messages
     */
    public static void renew() {
        instance = null;
    }

    /**
     * Gets instance of messages class
     * <p>(Singleton)</p>
     *
     * @return messages
     */
    public static Messages getInstance(SpaceChat context) {
        if (instance == null) {
            instance = new Messages(context);
        }
        return instance;
    }

    private final SpaceChat plugin;

    /* General */

    // help
    public Message generalHelp;

    /* Reload */

    // success
    public Message reloadSuccess;

    // failure
    public Message reloadFailure;
    /**
     * Broadcast
     */

    // args
    public Message broadcastArgs;

    // wrapper
    public Message broadcastWrapper;

    /**
     * Channel
     */

    // join
    public Message channelJoin;

    // leave
    public Message channelLeave;

    // listen
    public Message channelListen;

    // mute
    public Message channelMute;
    // invalid
    public Message channelInvalid;

    /**
     * Ignore
     */

    // player not found
    public Message playerNotFound;


    public Messages(SpaceChat plugin) {
        this.plugin = plugin;


        generalHelp = Message.fromConfigurationSection(this.getLangConfiguration(), "general.help")
                .build();
        reloadSuccess = Message.fromConfigurationSection(this.getLangConfiguration(), "reload.success")
                .build();
        reloadFailure = Message.fromConfigurationSection(this.getLangConfiguration(), "reload.failure")
                .build();
        broadcastArgs = Message.fromConfigurationSection(this.getLangConfiguration(), "broadcast.args")
                .build();
        broadcastWrapper = Message.fromConfigurationSection(this.getLangConfiguration(), "broadcast.wrapper")
                .build();
        channelJoin = Message.fromConfigurationSection(this.getLangConfiguration(), "channel.join")
                .build();
        channelLeave = Message.fromConfigurationSection(this.getLangConfiguration(), "channel.leave")
                .build();
        channelListen = Message.fromConfigurationSection(this.getLangConfiguration(), "channel.listen")
                .build();
        channelMute = Message.fromConfigurationSection(this.getLangConfiguration(), "channel.mute")
                .build();
        channelInvalid = Message.fromConfigurationSection(this.getLangConfiguration(), "channel.invalid")
                .build();
        playerNotFound = Message.fromConfigurationSection(this.getLangConfiguration(), "ignore.player-not-found")
                .build();
    }

    /**
     * Gets the lang configuration from the main class
     *
     * @return The lang configuration
     */
    private ConfigurationAdapter getLangConfiguration() {
        return plugin.getLangConfig().getAdapter();
    }
}

