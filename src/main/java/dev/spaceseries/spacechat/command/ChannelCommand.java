package dev.spaceseries.spacechat.command;

import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.command.SpaceChatCommand;
import org.bukkit.entity.Player;

@CommandPermission("space.chat.command.channel")
@CommandAlias("channel")
public class ChannelCommand extends SpaceChatCommand {

    public ChannelCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @Default
    @CatchUnknown
    @HelpCommand
    public void onCommand(Player player) {
        // send help message
        Messages.getInstance(plugin).generalHelp.message(player);
    }
}
