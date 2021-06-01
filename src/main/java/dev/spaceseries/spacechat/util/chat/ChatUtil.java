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
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogToType;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.model.formatting.Format;
import dev.spaceseries.spacechat.sync.redis.stream.packet.chat.RedisChatPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static dev.spaceseries.spacechat.config.Config.REDIS_SERVER_DISPLAYNAME;
import static dev.spaceseries.spacechat.config.Config.REDIS_SERVER_IDENTIFIER;

public class ChatUtil {

    /**
     * Send a chat message
     * <p>
     * This does the same thing as {@link ChatUtil#sendComponentMessage(Component)} but I just made it different for the sake
     * of understanding
     *
     * @param component component
     */
    public static void sendComponentChatMessage(Component component) {
        sendComponentMessage(component);
    }

    /**
     * Send a chat message to a specific player
     * <p>
     * This does the same thing as {@link ChatUtil#sendComponentMessage(Component, Player)} but I just made it different for the sake
     * of understanding
     *
     * @param component component
     * @param to        to
     */
    public static void sendComponentChatMessage(Component component, Player to) {
        sendComponentMessage(component, to);
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
     * Send a raw component to a player
     *
     * @param component component
     * @param to        to
     */
    public static void sendComponentMessage(Component component, Player to) {
        // send chat message to all online players
        Message.getAudienceProvider().player(to.getUniqueId()).sendMessage(component);
    }

    /**
     * Send a raw component to a channel
     *
     * @param component component
     * @param channel   channel
     */
    public static void sendComponentChannelMessage(Component component, Channel channel) {
        // get all subscribed players to that channel
        List<Player> playerList = SpaceChat.getInstance().getServerSyncServiceManager().getDataService().getSubscribedUUIDs(channel)
                .stream().map(Bukkit::getPlayer)
                .collect(Collectors.toList());

        // if a player in the list doesn't have permission to view it, then unsubscribe them
        Bukkit.getScheduler().runTaskAsynchronously(SpaceChat.getInstance(), () -> playerList.forEach(p -> {
            if (!p.hasPermission(channel.getPermission())) {
                SpaceChat.getInstance().getServerSyncServiceManager().getDataService().unsubscribeFromChannel(p.getUniqueId(), channel);
            }
        }));


        playerList.forEach(p -> sendComponentMessage(component, p));
    }

    /**
     * Send a chat message
     *
     * @param from    player that the message is from
     * @param message message
     * @param format  format
     * @param event   event
     */
    public static void sendChatMessage(Player from, String message, Format format, AsyncPlayerChatEvent event) {
        // get player's current channel, and send through that (if null, that means 'global')
        Channel applicableChannel = SpaceChat.getInstance().getServerSyncServiceManager().getDataService().getCurrentChannel(from.getUniqueId());

        dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component components;

        // if null, return
        if (format == null) {
            // build components default message
            // this only happens if it's not possible to find a chat format
            components = Component.text()
                    .append(Component.text(from.getDisplayName(), NamedTextColor.AQUA))
                    .append(Component.text("> ", NamedTextColor.GRAY))
                    .append(Component.text(message))
                    .build();
        } else { // if not null
            // get baseComponents from live builder
            components = new NormalLiveChatFormatBuilder().build(new Trio<>(from, message, format));
        }

        // if channel exists, then send through it
        if (applicableChannel != null) {
            sendComponentChannelMessage(components, applicableChannel);
        } else {
            // send component message to entire server
            sendComponentChatMessage(components);
        }

        // log to storage
        SpaceChat.getInstance()
                .getLogManagerImpl()
                .log(new LogChatWrap(LogType.CHAT, from.getName(), from.getUniqueId(), message, new Date()),
                        LogType.CHAT,
                        LogToType.STORAGE
                );

        // send via redis (it won't do anything if redis isn't enabled, so we can be sure that we aren't using dead methods that will throw an exception)
        SpaceChat.getInstance().getServerSyncServiceManager().getStreamService().publishChat(new RedisChatPacket(from.getUniqueId(), from.getName(), applicableChannel, REDIS_SERVER_IDENTIFIER.get(Config.get()), REDIS_SERVER_DISPLAYNAME.get(Config.get()), components));

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
     * @param from    player that the message is from
     * @param message message
     * @param format  format format
     * @param event   event
     */
    public static void sendRelationalChatMessage(Player from, String message, Format format, AsyncPlayerChatEvent event) {
        // component to use with storage and logging
        dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component sampledComponent;

        if (format == null) {
            // build components default message
            // this only happens if it's not possible to find a chat format
            sampledComponent = Component.text()
                    .append(Component.text(from.getDisplayName(), NamedTextColor.AQUA))
                    .append(Component.text("> ", NamedTextColor.GRAY))
                    .append(Component.text(message))
                    .build();
        } else { // if not null
            // get baseComponents from live builder
            sampledComponent = new NormalLiveChatFormatBuilder().build(new Trio<>(from, message, format));
        }

        // do relational parsing
        Bukkit.getOnlinePlayers().forEach(to -> {
            dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component component;

            if (format == null) {
                // build components default message
                // this only happens if it's not possible to find a chat format
                component = Component.text()
                        .append(Component.text(from.getDisplayName(), NamedTextColor.AQUA))
                        .append(Component.text("> ", NamedTextColor.GRAY))
                        .append(Component.text(message))
                        .build();
            } else { // if not null
                // get baseComponents from live builder
                component = new RelationalLiveChatFormatBuilder().build(new Quad<>(from, to, message, format));
            }

            // send to 'to-player'
            sendComponentChatMessage(component, to);
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
