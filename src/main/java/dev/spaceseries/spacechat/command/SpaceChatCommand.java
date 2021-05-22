package dev.spaceseries.spacechat.command;

import dev.spaceseries.spaceapi.command.Command;
import dev.spaceseries.spaceapi.command.Permissible;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.command.spacechat.ReloadCommand;

@Permissible("space.chat.command")
public class SpaceChatCommand extends Command {

    public SpaceChatCommand() {
        super(SpaceChat.getInstance().getPlugin(), "spacechat");

        // add sub commands
        addSubCommands(
                new ReloadCommand()
        );
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String s, String... args) {
        // send help message
        Messages.getInstance().generalHelp.msg(sender);
    }
}
