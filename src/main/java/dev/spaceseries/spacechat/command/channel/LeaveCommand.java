package dev.spaceseries.spacechat.command.channel;

import dev.spaceseries.spaceapi.command.Command;
import dev.spaceseries.spaceapi.command.Permissible;
import dev.spaceseries.spaceapi.command.PlayersOnly;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
import dev.spaceseries.spaceapi.text.Message;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.model.Channel;

@Permissible("space.chat.command.channel.leave")
@PlayersOnly
public class LeaveCommand extends Command {

    public LeaveCommand() {
        super(SpaceChat.getInstance().getPlugin(), "leave");
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String label, String... args) {
        // args
        if (args.length != 0) {
            Messages.getInstance().generalHelp.msg(sender);
            return;
        }

        // get current
        Channel current = SpaceChat.getInstance().getServerSyncServiceManager().getDataService().getCurrentChannel(sender.getUuid());

        // if current null
        if (current == null) {
            Messages.getInstance().generalHelp.msg(sender);
            return;
        }

        // update current channel (aka remove)
        SpaceChat.getInstance().getUserManager().use(sender.getUuid(), (user) -> {
            user.leaveChannel(current);

            // send message
            Messages.getInstance().channelLeave.msg(sender, "%channel%", current.getHandle());
        });
    }
}