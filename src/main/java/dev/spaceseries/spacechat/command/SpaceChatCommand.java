package dev.spaceseries.spacechat.command;

import dev.spaceseries.api.command.Command;
import dev.spaceseries.api.command.Permissible;
import dev.spaceseries.api.command.SpaceCommandSender;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;

@Permissible("space.chat")
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
        Messages.getInstance().GENERAL_HELP.msg(sender);
    }
}
