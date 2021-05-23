package dev.spaceseries.spacechat.manager;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.loader.ChatFormatLoader;
import dev.spaceseries.spacechat.loader.FormatLoader;
import dev.spaceseries.spacechat.loader.FormatManager;
import dev.spaceseries.spacechat.model.FormatType;
import dev.spaceseries.spacechat.model.ChatFormat;
import dev.spaceseries.spacechat.util.chat.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Comparator;
import java.util.Locale;

import static dev.spaceseries.spacechat.config.Config.USE_RELATIONAL_PLACEHOLDERS;

public class ChatFormatManager extends FormatManager<ChatFormat> {

    /**
     * The format loader
     */
    private final FormatLoader<ChatFormat> chatFormatFormatLoader;

    /**
     * Initializes
     */
    public ChatFormatManager() {
        // create format manager
        this.chatFormatFormatLoader = new ChatFormatLoader(SpaceChat.getInstance()
                .getFormatsConfig()
                .getConfig()
                .getSection(
                        FormatType.CHAT.getSectionKey().toLowerCase(Locale.ROOT)
                )
        );

        // load
        this.loadFormats();
    }

    /**
     * Loads formats
     */
    public void loadFormats() {
        chatFormatFormatLoader.load(this);
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
        ChatFormat applicableFormat = getAll()
                .values()
                .stream()
                .filter(format -> player.hasPermission(format.getPermission()) || format.getHandle().equals("default")) // player has permission OR the format is default
                .max(Comparator.comparing(ChatFormat::getPriority))
                .orElse(null);

        // if relational
        if (USE_RELATIONAL_PLACEHOLDERS.get(Config.get()) && !SpaceChat.getInstance().getServerSyncServiceManager().isUsingNetwork()) {
            // send relational
            ChatUtil.sendRelationalChatMessage(player, message, applicableFormat, event);
        } else {
            ChatUtil.sendChatMessage(player, message, applicableFormat, event);
        }
    }
}
