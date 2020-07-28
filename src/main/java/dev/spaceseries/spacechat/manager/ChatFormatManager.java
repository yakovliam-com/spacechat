package dev.spaceseries.spacechat.manager;

import dev.spaceseries.api.util.ColorUtil;
import dev.spaceseries.api.util.Trio;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.builder.ReflectionHelper;
import dev.spaceseries.spacechat.builder.live.LiveChatFormatBuilder;
import dev.spaceseries.spacechat.loader.FormatType;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.model.Format;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Comparator;
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
        Format applicableFormat = getAll().values().stream()
                .filter(format -> player.hasPermission(format.getPermission()) || format.getHandle().equals("default")) // player has permission OR the format is default
                .max(Comparator.comparing(Format::getPriority))
                .orElse(null);

        // create components array
        BaseComponent[] components;

        // if null, return
        if (applicableFormat == null) {
            // build components default message
            components = new ComponentBuilder("")
                    .append(ChatColor.AQUA + player.getDisplayName() + ChatColor.GRAY + "> ")
                    .append(ColorUtil.translateFromAmpersand(message))
                    .create();
        } else { // if not null
            // get baseComponents from live builder
            components = new LiveChatFormatBuilder().build(new Trio<>(player, message, applicableFormat));
        }

        // log
        SpaceChat.getInstance()
                .getLogManager().log(Arrays.stream(components)
                        .map(BaseComponent::toLegacyText)
                        .collect(Collectors.joining()),
                LogType.CHAT);

        // get all online players, loop through, send chat message
//        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.spigot().sendMessage(components));
        ReflectionHelper.sendPacket(ReflectionHelper.createTextPacket(ComponentSerializer.toString(components)), Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }
}
