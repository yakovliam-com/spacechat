package com.yakovliam.spacechat.manager;

import com.yakovliam.spacechat.loader.FormatType;
import com.yakovliam.spacechat.model.Format;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class ChatFormatManager extends FormatManager {

    /**
     * Initializes
     */
    public ChatFormatManager() {
        super(FormatType.CHAT);
    }

    /**
     * Sends a chat message using the applicable format
     *
     * @param player  The player
     * @param message The message
     */
    public void send(Player player, String message) {

        // get applicable format
        Format applicableFormat = getAll().values().stream()
                .filter(format -> player.hasPermission(format.getPermission()) || format.getHandle().equals("default")) // player has permission OR the format is default
                .max(Comparator.comparing(Format::getPriority))
                .orElse(null);

        //todo implement LIVE building

        // send chat packet
    }
}
