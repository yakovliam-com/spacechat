package dev.spaceseries.spacechat.command;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.MessageType;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.command.channel.JoinCommand;
import dev.spaceseries.spacechat.command.channel.LeaveCommand;
import dev.spaceseries.spacechat.command.channel.ListenCommand;
import dev.spaceseries.spacechat.command.channel.MuteCommand;
import dev.spaceseries.spacechat.command.spacechat.ReloadCommand;
import org.bukkit.ChatColor;

import java.util.Arrays;

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
                new ReloadCommand(plugin),
                new ChannelCommand(plugin),
                new JoinCommand(plugin),
                new LeaveCommand(plugin),
                new ListenCommand(plugin),
                new MuteCommand(plugin),
                new BroadcastCommand(plugin),
                new BroadcastMinimessageCommand(plugin)
        ).forEach(this::registerCommand);
    }
}
