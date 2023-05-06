package dev.spaceseries.spacechat.command;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.MessageType;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.stream.Collectors;

public class CommandManager extends BukkitCommandManager {

    public CommandManager(SpaceChatPlugin plugin) {
        super(plugin);

        //enableUnstableAPI("help");
        //enableUnstableAPI("brigadier"); This is necessary???

        setFormat(MessageType.INFO, ChatColor.WHITE);
        setFormat(MessageType.HELP, ChatColor.GRAY);
        setFormat(MessageType.ERROR, ChatColor.RED);
        setFormat(MessageType.SYNTAX, ChatColor.GRAY);

        getCommandCompletions().registerCompletion("chatplayers", context -> Collections.unmodifiableSet(plugin.getUserManager().getOnlinePlayers().keySet()));
        getCommandCompletions().registerCompletion("ignoredplayers", context -> plugin.getUserManager().getIgnoredList(context.getPlayer().getName()).stream().limit(10).collect(Collectors.toList()));

        registerCommand(new SpaceChatCommand(plugin), true);
        registerCommand(new ChannelCommand(plugin));
        registerCommand(new BroadcastCommand(plugin));
        registerCommand(new BroadcastMinimessageCommand(plugin));
        registerCommand(new IgnoreCommand(plugin));
    }
}
