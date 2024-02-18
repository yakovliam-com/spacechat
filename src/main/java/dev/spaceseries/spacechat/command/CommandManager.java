package dev.spaceseries.spacechat.command;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.MessageType;
import com.saicone.ezlib.Dependency;
import com.saicone.ezlib.Repository;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.stream.Collectors;

@Dependency(value = "co.aikar:acf-bukkit:0.5.1-SNAPSHOT", snapshot = true,
        repository = @Repository(url = "https://repo.aikar.co/content/groups/aikar/"),
        relocate = {
                "co.aikar.commands", "{package}.lib.acf.commands",
                "co.aikar.locales", "{package}.lib.acf.locales"
        }
)
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
