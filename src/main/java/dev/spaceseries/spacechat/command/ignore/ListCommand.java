package dev.spaceseries.spacechat.command.ignore;

import dev.spaceseries.spaceapi.command.*;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.model.User;

@SubCommand
@PlayersOnly
@Permissible("space.chat.command.ignore.list")
public class ListCommand extends Command {

    private final SpaceChat plugin;

    public ListCommand(SpaceChat plugin) {
        super(plugin.getPlugin(), "list");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String label, String... args) {
        // args
        if (args.length != 0) {
            Messages.getInstance(plugin).generalHelp.msg(sender);
            return;
        }

        // TODO Depending on decided storage method, pull all ignored players and list them in a message
    }
}
