package dev.spaceseries.spacechat.command.ignore;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.command.SpaceChatCommand;
import org.bukkit.entity.Player;

@Subcommand("list")
@CommandAlias("ignore")
@CommandPermission("space.chat.command.ignore.list")
public class ListCommand extends SpaceChatCommand {

    public ListCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @Default
    public void onList(Player player) {
        // TODO Depending on decided storage method, pull all ignored players and list them in a message
    }
}
