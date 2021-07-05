package dev.spaceseries.spacechat.command.ignore;

import dev.spaceseries.spaceapi.command.*;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.model.User;
import org.bukkit.OfflinePlayer;

@SubCommand
@PlayersOnly
@Permissible("space.chat.command.ignore.add")
public class AddCommand extends Command {

    private final SpaceChat plugin;

    public AddCommand(SpaceChat plugin) {
        super(plugin.getPlugin(), "add");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String label, String... args) {
        // args
        if (args.length != 1) {
            Messages.getInstance(plugin).generalHelp.msg(sender);
            return;
        }

        // get user
        plugin.getUserManager().getByName(args[0], (user -> {
            if (user == null) {
                Messages.getInstance(plugin).playerNotFound.msg(sender);
                return;
            }

            // TODO Implement adding the target from the decided storage method
        }));
    }
}
