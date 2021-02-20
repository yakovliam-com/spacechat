package dev.spaceseries.spacechat.util.chat;

import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.serializer.legacy.LegacyComponentSerializer;
import dev.spaceseries.spaceapi.text.Message;
import dev.spaceseries.spaceapi.util.ColorUtil;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.logging.wrap.LogToType;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.stream.Collectors;

public class ChatUtil {

    /**
     * Send a chat message
     *
     * @param component component
     */
    public static void sendComponentChatMessage(Component component) {
        sendComponentChatMessage(component, null);
    }

    /**
     * Send a chat message
     *
     * @param component component
     * @param event     event
     */
    public static void sendComponentChatMessage(Component component, AsyncPlayerChatEvent event) {
        // get all online players, loop through, send chat message
        Message.getAudienceProvider().players().sendMessage(component);

        // log to console
        if (event != null) // if there's an event, log w/ the event
            SpaceChat.getInstance()
                    .getLogManagerImpl()
                    .log(component.children()
                            .stream()
                            .map(c -> LegacyComponentSerializer.legacySection().serialize(c))
                            .map(ColorUtil::translateFromAmpersand)
                            .map(ColorUtil::stripColor)
                            .collect(Collectors.joining()), LogType.CHAT, LogToType.CONSOLE, event);
        else
            SpaceChat.getInstance() // if there's no event, just log to console without using the event
                    .getLogManagerImpl()
                    .log(component.children()
                            .stream()
                            .map(c -> LegacyComponentSerializer.legacySection().serialize(c))
                            .map(ColorUtil::translateFromAmpersand)
                            .map(ColorUtil::stripColor)
                            .collect(Collectors.joining()), LogType.CHAT, LogToType.CONSOLE);

        // note: storage logging is handled in the actual chat format manager because there's no need to log
        // if a message come from redis. This is really a generified version of my initial idea
        // but it's pretty good and it works
    }
}
