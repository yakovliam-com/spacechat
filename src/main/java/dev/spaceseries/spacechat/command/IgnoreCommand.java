package dev.spaceseries.spacechat.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
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

    @Subcommand("add")
    @CommandAlias("ignore")
    @CommandPermission("space.chat.command.ignore.add")
    public class AddCommand extends BaseCommand {

        @Default
        public void onAdd(Player player, @Single String targetName) {
            // get user
            plugin.getUserManager().getByName(targetName, (user -> {
                if (user == null) {
                    Messages.getInstance(plugin).playerNotFound.message(player);
                    return;
                }

                // TODO Implement adding the target from the decided storage method
            }));
        }
    }

    @Subcommand("list")
    @CommandAlias("ignore")
    @CommandPermission("space.chat.command.ignore.list")
    public class ListCommand extends BaseCommand {

        @Default
        public void onList(Player player) {
            // TODO Depending on decided storage method, pull all ignored players and list them in a message
        }
    }

    @Subcommand("remove")
    @CommandAlias("ignore")
    @CommandPermission("space.chat.command.ignore.remove")
    public class RemoveCommand extends BaseCommand {

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

    @Default
    @CatchUnknown
    @HelpCommand
    public void onDefault(Player player) {
        // send help message
        Messages.getInstance(plugin).generalHelp.message(player);
    }
}
