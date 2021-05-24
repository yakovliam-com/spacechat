package dev.spaceseries.spacechat.command.channel;

import dev.spaceseries.spaceapi.command.Command;
import dev.spaceseries.spaceapi.command.Permissible;
import dev.spaceseries.spaceapi.command.PlayersOnly;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
import dev.spaceseries.spaceapi.text.Message;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.model.Channel;

@Permissible("space.chat.command.channel.join")
@PlayersOnly
public class JoinCommand extends Command {

    public JoinCommand() {
        super(SpaceChat.getInstance().getPlugin(), "join");
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String label, String... args) {
        // args
        if (args.length != 1) {
            Messages.getInstance().generalHelp.msg(sender);
            return;
        }

        String channel = args[0];

        // get channel
        Channel applicable = SpaceChat.getInstance().getChannelManager().get(channel, null);
        if (applicable == null) {
            // send message
            Messages.getInstance().channelInvalid.msg(sender, "%channel%", channel);
            return;
        }

        // do they have permission?
        if (!sender.hasPermission(applicable.getPermission())) {
            Message.Global.ACCESS_DENIED.msg(sender);
            return;
        }

        // set current channel
        SpaceChat.getInstance().getServerSyncServiceManager().getDataService().updateCurrentChannel(sender.getUuid(), applicable);

        // send message
        Messages.getInstance().channelJoin.msg(sender, "%channel%", channel);
    }
}