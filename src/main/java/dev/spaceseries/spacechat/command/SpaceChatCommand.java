package dev.spaceseries.spacechat.command;

import dev.spaceseries.spaceapi.command.Command;
import dev.spaceseries.spaceapi.command.Permissible;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.command.spacechat.ReloadCommand;

@Permissible("space.chat.command")
public class SpaceChatCommand extends Command {

    private final SpaceChat plugin;

    public SpaceChatCommand(SpaceChat plugin) {
        super(plugin.getPlugin(), "spacechat");

        this.plugin = plugin;

        // add sub commands
        addSubCommands(
                new ReloadCommand(plugin)
        );
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String s, String... args) {
        // send help message
        Messages.getInstance(plugin).generalHelp.msg(sender);
    }
}
