package dev.spaceseries.spacechat.manager;

import dev.spaceseries.spaceapi.text.Message;
import dev.spaceseries.spaceapi.util.ColorUtil;
import dev.spaceseries.spaceapi.util.Trio;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.builder.live.LiveChatFormatBuilder;
import dev.spaceseries.spacechat.loader.FormatType;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogToType;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.model.Format;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.format.NamedTextColor;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

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
     * @param event  The event
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

        // create components
        dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component components;

        // if null, return
        if (applicableFormat == null) {
            // build components default message
            // this only happens if it's not possible to find a chat format
            components = Component.text()
                    .append(Component.text(player.getDisplayName(), NamedTextColor.AQUA))
                    .append(Component.text("> ", NamedTextColor.GRAY))
                    .append(Component.text(message))
                    .build();
        } else { // if not null
            // get baseComponents from live builder
            components = new LiveChatFormatBuilder().build(new Trio<>(player, message, applicableFormat));
        }

        // get all online players, loop through, send chat message
        Message.getAudienceProvider().players().sendMessage(components);

        // log to console
        //noinspection deprecation
        SpaceChat.getInstance()
                .getLogManagerImpl()
                .log(components.children()
                        .stream()
                        .map(c -> LegacyComponentSerializer.legacySection().serialize(c))
                        .map(ColorUtil::translateFromAmpersand)
                        .map(ColorUtil::stripColor)
                        .collect(Collectors.joining()), LogType.CHAT, LogToType.CONSOLE, event);

        // async logging
        Bukkit.getScheduler().runTaskAsynchronously(SpaceChat.getInstance(), () -> {
            // log to storage
            SpaceChat.getInstance()
                    .getLogManagerImpl()
                    .log(new LogChatWrap(LogType.CHAT, player.getName(), player.getUniqueId(), message, new Date()),
                            LogType.CHAT,
                            LogToType.STORAGE
                    );
        });
    }
}
