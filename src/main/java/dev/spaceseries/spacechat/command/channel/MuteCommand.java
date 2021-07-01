package dev.spaceseries.spacechat.command.channel;

import dev.spaceseries.spaceapi.command.Command;
import dev.spaceseries.spaceapi.command.Permissible;
import dev.spaceseries.spaceapi.command.PlayersOnly;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
 import dev.spaceseries.spacechat.api.message.Message;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.model.Channel;

@Permissible("space.chat.command.channel.mute")
@PlayersOnly
public class MuteCommand extends Command {

    private final SpaceChat plugin;

    public MuteCommand(SpaceChat plugin) {
        super(plugin.getPlugin(), "mute");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String label, String... args) {
        // args
        if (args.length != 1) {
            Messages.getInstance(plugin).generalHelp.msg(sender);
            return;
        }

        String channel = args[0];

        // get channel
        Channel applicable = plugin.getChannelManager().get(channel, null);
        if (applicable == null) {
            // send message
            Messages.getInstance(plugin).channelInvalid.msg(sender, "%channel%", channel);
            return;
        }

        // do they have permission?
        if (!sender.hasPermission(applicable.getPermission())) {
            Message.Global.ACCESS_DENIED.msg(sender);
            return;
        }

        // set current channel
        plugin.getUserManager().use(sender.getUuid(), (user) -> {
            user.unsubscribeFromChannel(applicable);

            // send message
            Messages.getInstance(plugin).channelMute.msg(sender, "%channel%", channel);
        });
    }
}