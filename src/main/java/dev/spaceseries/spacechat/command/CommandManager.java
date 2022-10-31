package dev.spaceseries.spacechat.command;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.MessageType;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.User;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandManager extends BukkitCommandManager {

    public CommandManager(SpaceChatPlugin plugin) {
        super(plugin);

        enableUnstableAPI("help");
        enableUnstableAPI("brigadier");

        setFormat(MessageType.INFO, ChatColor.WHITE);
        setFormat(MessageType.HELP, ChatColor.GRAY);
        setFormat(MessageType.ERROR, ChatColor.RED);
        setFormat(MessageType.SYNTAX, ChatColor.GRAY);

        Arrays.asList(
                new SpaceChatCommand(plugin),
                new ChannelCommand(plugin),
                new BroadcastCommand(plugin),
                new BroadcastMinimessageCommand(plugin),
                new MessageCommand(plugin),
                new ReplyCommand(plugin)
        ).forEach(this::registerCommand);
    }
}
