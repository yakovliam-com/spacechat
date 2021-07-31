package dev.spaceseries.spacechat.command;

import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import org.bukkit.command.CommandSender;

@CommandAlias("spacechat")
@CommandPermission("space.chat.command")
public class SpaceChatCommand extends dev.spaceseries.spacechat.api.command.SpaceChatCommand {

    public SpaceChatCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @Default
    @CatchUnknown
    @HelpCommand
    public void onDefault(CommandSender sender) {
        // send help message
        Messages.getInstance(plugin).generalHelp.message(sender);
    }
}
