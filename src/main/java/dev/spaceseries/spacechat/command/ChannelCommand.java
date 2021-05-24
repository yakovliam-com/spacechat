package dev.spaceseries.spacechat.command;

import dev.spaceseries.spaceapi.command.Command;
import dev.spaceseries.spaceapi.command.Permissible;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.command.channel.JoinCommand;
import dev.spaceseries.spacechat.command.channel.LeaveCommand;
import dev.spaceseries.spacechat.command.channel.ListenCommand;
import dev.spaceseries.spacechat.command.channel.MuteCommand;

@Permissible("space.chat.command.channel")
public class ChannelCommand extends Command {

    public ChannelCommand() {
        super(SpaceChat.getInstance().getPlugin(), "channel");

        // add sub commands
        addSubCommands(
                new JoinCommand(),
                new LeaveCommand(),
                new ListenCommand(),
                new MuteCommand()
        );
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String label, String... args) {
        // send help message
        Messages.getInstance().generalHelp.msg(sender);
    }
}
