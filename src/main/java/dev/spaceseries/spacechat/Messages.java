package dev.spaceseries.spacechat;

import dev.spaceseries.spaceapi.config.impl.Configuration;
import dev.spaceseries.spaceapi.text.Message;

public class Messages {

    /**
     * Instance of this class
     */
    private static Messages instance;

    /**
     * Gets instance of messages class
     * <p>(Singleton)</p>
     *
     * @return messages
     */
    public static Messages getInstance() {
        if (instance == null)
            instance = new Messages();
        return instance;
    }


    /**
     * Renews the messages
     */
    public static void renew() {
        instance = null;
    }

    /* General */

    // help
    public Message generalHelp = Message.fromConfigurationSection(getLangConfiguration().getSection("general.help"), "general.help")
            .build();

    /* Reload */

    // success
    public Message reloadSuccess = Message.fromConfigurationSection(getLangConfiguration().getSection("reload.success"), "reload.success")
            .build();

    // failure
    public Message reloadFailure = Message.fromConfigurationSection(getLangConfiguration().getSection("reload.failure"), "reload.failure")
            .build();

    /**
     * Broadcast
     */

    // args
    public Message broadcastArgs = Message.fromConfigurationSection(getLangConfiguration().getSection("broadcast.args"), "broadcast.args")
            .build();

    // wrapper
    public Message broadcastWrapper = Message.fromConfigurationSection(getLangConfiguration().getSection("broadcast.wrapper"), "broadcast.wrapper")
            .build();

    /**
     * Channel
     */

    // join
    public Message channelJoin = Message.fromConfigurationSection(getLangConfiguration().getSection("channel.join"), "channel.join")
            .build();

    // leave
    public Message channelLeave = Message.fromConfigurationSection(getLangConfiguration().getSection("channel.leave"), "channel.leave")
            .build();

    // listen
    public Message channelListen = Message.fromConfigurationSection(getLangConfiguration().getSection("channel.listen"), "channel.listen")
            .build();

    // mute
    public Message channelMute = Message.fromConfigurationSection(getLangConfiguration().getSection("channel.mute"), "channel.mute")
            .build();

    // invalid
    public Message channelInvalid = Message.fromConfigurationSection(getLangConfiguration().getSection("channel.invalid"), "channel.invalid")
            .build();

    /**
     * Gets the lang configuration from the main class
     *
     * @return The lang configuration
     */
    private Configuration getLangConfiguration() {
        return SpaceChat.getInstance().getLangConfig().getConfig();
    }
}

