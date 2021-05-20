package dev.spaceseries.spacechat.manager;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.loader.FormatType;
import dev.spaceseries.spacechat.model.Format;
import dev.spaceseries.spacechat.util.chat.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Comparator;

import static dev.spaceseries.spacechat.config.Config.USE_RELATIONAL_PLACEHOLDERS;

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
     * @param event   The event
     * @param message The message
     */
    public void send(AsyncPlayerChatEvent event, String message) {
        // get player
        Player player = event.getPlayer();

        // get applicable format
        Format applicableFormat = getAll()
                .values()
                .stream()
                .filter(format -> player.hasPermission(format.getPermission()) || format.getHandle().equals("default")) // player has permission OR the format is default
                .max(Comparator.comparing(Format::getPriority))
                .orElse(null);

        // if relational
        if (USE_RELATIONAL_PLACEHOLDERS.get(Config.get()) && SpaceChat.getInstance().getMessagingService().isImplemented()) {
            // send relational
            ChatUtil.sendRelationalChatMessage(player, message, applicableFormat, event);
        } else {
            ChatUtil.sendChatMessage(player, message, applicableFormat, event);
        }
    }
}
