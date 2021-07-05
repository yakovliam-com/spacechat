package dev.spaceseries.spacechat.command;

import dev.spaceseries.spaceapi.command.Command;
import dev.spaceseries.spaceapi.command.Permissible;
import dev.spaceseries.spaceapi.command.PlayersOnly;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.command.ignore.AddCommand;
import dev.spaceseries.spacechat.command.ignore.ListCommand;
import dev.spaceseries.spacechat.command.ignore.RemoveCommand;

@PlayersOnly
@Permissible("space.chat.command.ignore")
public class IgnoreCommand extends Command {

    private SpaceChat plugin;

    public IgnoreCommand(SpaceChat plugin) {
        super(plugin.getPlugin(), "ignore");

        // add sub commands
        addSubCommands(
                new AddCommand(plugin),
                new RemoveCommand(plugin),
                new ListCommand(plugin)
        );
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String label, String... args) {
        // send help message
        Messages.getInstance(plugin).generalHelp.msg(sender);
    }
}
