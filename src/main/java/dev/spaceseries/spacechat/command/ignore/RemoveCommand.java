package dev.spaceseries.spacechat.command.ignore;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.command.SpaceChatCommand;
import org.bukkit.entity.Player;

@Subcommand("remove")
@CommandAlias("ignore")
@CommandPermission("space.chat.command.ignore.remove")
public class RemoveCommand extends SpaceChatCommand {

    public RemoveCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @Default
    public void onRemove(Player player, @Single String targetName) {
        // get user
        plugin.getUserManager().getByName(targetName, (user) -> {
            if (user == null) {
                Messages.getInstance(plugin).playerNotFound.message(player);
                return;
            }

            // TODO Implement removing the target from the decided storage method -- also check if the sender even has ignored the target

        });
    }
}
