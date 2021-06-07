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

    private final SpaceChat plugin;

    public ChannelCommand(SpaceChat plugin) {
        super(plugin.getPlugin(), "channel");
        this.plugin = plugin;

        // add sub commands
        addSubCommands(
                new JoinCommand(plugin),
                new LeaveCommand(plugin),
                new ListenCommand(plugin),
                new MuteCommand(plugin)
        );
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String label, String... args) {
        // send help message
        Messages.getInstance(plugin).generalHelp.msg(sender);
    }
}
