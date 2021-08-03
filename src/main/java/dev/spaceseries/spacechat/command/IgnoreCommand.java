package dev.spaceseries.spacechat.command;

import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import org.bukkit.entity.Player;

@CommandPermission("space.chat.command.ignore")
@CommandAlias("ignore")
public class IgnoreCommand extends SpaceChatCommand {

    private SpaceChatPlugin plugin;

    public IgnoreCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @Default
    @CatchUnknown
    @HelpCommand
    public void onDefault(Player player) {
        // send help message
        Messages.getInstance(plugin).generalHelp.message(player);
    }
}
