package dev.spaceseries.spacechat.manager;

import dev.spaceseries.spaceapi.util.Trio;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.builder.live.LiveChatFormatBuilder;
import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.messaging.redis.packet.chat.RedisChatPacket;
import dev.spaceseries.spacechat.loader.FormatType;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogToType;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.model.Format;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.format.NamedTextColor;
import dev.spaceseries.spacechat.util.chat.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Comparator;
import java.util.Date;

import static dev.spaceseries.spacechat.configuration.Config.*;

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

        // send chat message!
        ChatUtil.sendComponentChatMessage(components, event);

        // send via redis (it won't do anything if redis isn't enabled, so we can be sure that we aren't using dead methods that will throw an exception)
        SpaceChat.getInstance().getMessagingService().getSupervisor().publishChatMessage(new RedisChatPacket(player.getUniqueId(), player.getName(), REDIS_SERVER_IDENTIFIER.get(Config.get()), REDIS_SERVER_DISPLAYNAME.get(Config.get()), components));

        // log to storage
        SpaceChat.getInstance()
                .getLogManagerImpl()
                .log(new LogChatWrap(LogType.CHAT, player.getName(), player.getUniqueId(), message, new Date()),
                        LogType.CHAT,
                        LogToType.STORAGE
                );
    }
}
