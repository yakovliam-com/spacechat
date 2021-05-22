package dev.spaceseries.spacechat.util.chat;

import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.format.NamedTextColor;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.serializer.legacy.LegacyComponentSerializer;
import dev.spaceseries.spaceapi.text.Message;
import dev.spaceseries.spaceapi.util.ColorUtil;
import dev.spaceseries.spaceapi.util.Quad;
import dev.spaceseries.spaceapi.util.Trio;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.builder.live.NormalLiveChatFormatBuilder;
import dev.spaceseries.spacechat.builder.live.RelationalLiveChatFormatBuilder;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogToType;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.model.ChatFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Date;
import java.util.stream.Collectors;

public class ChatUtil {

    /**
     * Send a chat message
     *
     * @param component component
     */
    public static void sendComponentChatMessage(Component component) {
        sendComponentMessage(component);
    }

    /**
     * Send a raw component to all players
     *
     * @param component component
     */
    public static void sendComponentMessage(Component component) {
        // send chat message to all online players
        Message.getAudienceProvider().players().sendMessage(component);
    }

    /**
     * Send a chat message
     *
     * @param from       player that the message is from
     * @param message    message
     * @param applicable applicable format
     * @param event      event
     */
    public static void sendChatMessage(Player from, String message, ChatFormat applicable, AsyncPlayerChatEvent event) {
        dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component components;

        // if null, return
        if (applicable == null) {
            // build components default message
            // this only happens if it's not possible to find a chat format
            components = Component.text()
                    .append(Component.text(from.getDisplayName(), NamedTextColor.AQUA))
                    .append(Component.text("> ", NamedTextColor.GRAY))
                    .append(Component.text(message))
                    .build();
        } else { // if not null
            // get baseComponents from live builder
            components = new NormalLiveChatFormatBuilder().build(new Trio<>(from, message, applicable.getFormat()));
        }

        // get all online players, loop through, send chat message
        Message.getAudienceProvider().players().sendMessage(components);

        // log to storage
        SpaceChat.getInstance()
                .getLogManagerImpl()
                .log(new LogChatWrap(LogType.CHAT, from.getName(), from.getUniqueId(), message, new Date()),
                        LogType.CHAT,
                        LogToType.STORAGE
                );

        // log to console
        if (event != null) // if there's an event, log w/ the event
            SpaceChat.getInstance()
                    .getLogManagerImpl()
                    .log(components.children()
                            .stream()
                            .map(c -> LegacyComponentSerializer.legacySection().serialize(c))
                            .map(ColorUtil::translateFromAmpersand)
                            .map(ColorUtil::stripColor)
                            .collect(Collectors.joining()), LogType.CHAT, LogToType.CONSOLE, event);
        else
            SpaceChat.getInstance() // if there's no event, just log to console without using the event
                    .getLogManagerImpl()
                    .log(components.children()
                            .stream()
                            .map(c -> LegacyComponentSerializer.legacySection().serialize(c))
                            .map(ColorUtil::translateFromAmpersand)
                            .map(ColorUtil::stripColor)
                            .collect(Collectors.joining()), LogType.CHAT, LogToType.CONSOLE);

        // note: storage logging is handled in the actual chat format manager because there's no need to log
        // if a message come from redis. This is really a generified version of my initial idea
        // but it's pretty good and it works
    }


    /**
     * Send a chat message with relational placeholders
     *
     * @param from       player that the message is from
     * @param message    message
     * @param applicable applicable format
     * @param event      event
     */
    public static void sendRelationalChatMessage(Player from, String message, ChatFormat applicable, AsyncPlayerChatEvent event) {
        // component to use with storage and logging
        dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component sampledComponent;

        if (applicable == null) {
            // build components default message
            // this only happens if it's not possible to find a chat format
            sampledComponent = Component.text()
                    .append(Component.text(from.getDisplayName(), NamedTextColor.AQUA))
                    .append(Component.text("> ", NamedTextColor.GRAY))
                    .append(Component.text(message))
                    .build();
        } else { // if not null
            // get baseComponents from live builder
            sampledComponent = new NormalLiveChatFormatBuilder().build(new Trio<>(from, message, applicable.getFormat()));
        }

        // do relational parsing
        Bukkit.getOnlinePlayers().forEach(to -> {
            dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component component;

            if (applicable == null) {
                // build components default message
                // this only happens if it's not possible to find a chat format
                component = Component.text()
                        .append(Component.text(from.getDisplayName(), NamedTextColor.AQUA))
                        .append(Component.text("> ", NamedTextColor.GRAY))
                        .append(Component.text(message))
                        .build();
            } else { // if not null
                // get baseComponents from live builder
                component = new RelationalLiveChatFormatBuilder().build(new Quad<>(from, to, message, applicable.getFormat()));
            }

            // send to 'to-player'
            Message.getAudienceProvider().player(to.getUniqueId()).sendMessage(component);
        });

        // log to storage
        SpaceChat.getInstance()
                .getLogManagerImpl()
                .log(new LogChatWrap(LogType.CHAT, from.getName(), from.getUniqueId(), message, new Date()),
                        LogType.CHAT,
                        LogToType.STORAGE
                );

        // log to console
        if (event != null) // if there's an event, log w/ the event
            SpaceChat.getInstance()
                    .getLogManagerImpl()
                    .log(sampledComponent.children()
                            .stream()
                            .map(c -> LegacyComponentSerializer.legacySection().serialize(c))
                            .map(ColorUtil::translateFromAmpersand)
                            .map(ColorUtil::stripColor)
                            .collect(Collectors.joining()), LogType.CHAT, LogToType.CONSOLE, event);
        else
            SpaceChat.getInstance() // if there's no event, just log to console without using the event
                    .getLogManagerImpl()
                    .log(sampledComponent.children()
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
