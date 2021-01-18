package dev.spaceseries.spacechat.manager;

import dev.spaceseries.api.text.Message;
import dev.spaceseries.api.util.ColorUtil;
import dev.spaceseries.api.util.Trio;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.builder.live.LiveChatFormatBuilder;
import dev.spaceseries.spacechat.loader.FormatType;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogToType;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.model.Format;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
     * @param player  The player
     * @param message The message
     */
    public void send(Player player, String message) {
        // get applicable format
        Format applicableFormat = getAll()
                .values()
                .stream()
                .filter(format -> player.hasPermission(format.getPermission()) || format.getHandle().equals("default")) // player has permission OR the format is default
                .max(Comparator.comparing(Format::getPriority))
                .orElse(null);

        // create components
        net.kyori.adventure.text.Component components;

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

        // async logging
        Bukkit.getScheduler().runTaskAsynchronously(SpaceChat.getInstance(), () -> {
            // log to console
            SpaceChat.getInstance()
                    .getLogManagerImpl()
                    .log(components.children()
                            .stream()
                            .map(c -> LegacyComponentSerializer.legacySection().serialize(c))
                            .map(ColorUtil::translateFromAmpersand)
                            .map(ColorUtil::stripColor)
                            .collect(Collectors.joining()), LogType.CHAT, LogToType.CONSOLE);

            // log to storage
            SpaceChat.getInstance()
                    .getLogManagerImpl()
                    .log(new LogChatWrap(LogType.CHAT, player.getName(), player.getUniqueId(), message, new Date()),
                            LogType.CHAT,
                            LogToType.STORAGE
                    );
        });

        // get all online players, loop through, send chat message

        Message.getAudienceProvider().players().sendMessage(components);

        // String json = GsonComponentSerializer.gson().serialize(components);
        //  ReflectionHelper.sendPacket(ReflectionHelper.createTextPacket(json), Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }
}
