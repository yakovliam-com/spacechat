package dev.spaceseries.spacechat.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.command.SpaceChatCommand;
import dev.spaceseries.spacechat.model.Channel;
import org.bukkit.entity.Player;

@CommandPermission("space.chat.command.channel")
@CommandAlias("channel")
public class ChannelCommand extends SpaceChatCommand {

    public ChannelCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @CommandPermission("space.chat.command.channel.mute")
    @CommandAlias("channel")
    @Subcommand("mute")
    public class MuteCommand extends BaseCommand {

        @Default
        public void onMute(Player player, @Single String channel) {
            // get channel
            Channel applicable = plugin.getChannelManager().get(channel, null);
            if (applicable == null) {
                // send message
                Messages.getInstance(plugin).channelInvalid.message(player, "%channel%", channel);
                return;
            }

            // do they have permission?
            if (!player.hasPermission(applicable.getPermission())) {
                Messages.getInstance(plugin).channelAccessDenied.message(player);
                return;
            }

            // set current channel
            plugin.getUserManager().use(player.getUniqueId(), (user) -> {
                user.unsubscribeFromChannel(applicable);

                // send message
                Messages.getInstance(plugin).channelMute.message(player, "%channel%", channel);
            });
        }
    }

    @CommandPermission("space.chat.command.channel.listen")
    @Subcommand("listen")
    @CommandAlias("channel")
    public class ListenCommand extends BaseCommand {

        @Default
        public void onListen(Player player, @Single String channel) {
            // get channel
            Channel applicable = plugin.getChannelManager().get(channel, null);
            if (applicable == null) {
                // send message
                Messages.getInstance(plugin).channelInvalid.message(player, "%channel%", channel);
                return;
            }

            // do they have permission?
            if (!player.hasPermission(applicable.getPermission())) {
                Messages.getInstance(plugin).channelAccessDenied.message(player);
                return;
            }

            // set current channel
            plugin.getUserManager().use(player.getUniqueId(), (user) -> {
                user.subscribeToChannel(applicable);

                // send message
                Messages.getInstance(plugin).channelListen.message(player, "%channel%", channel);
            });
        }
    }

    @CommandPermission("space.chat.command.channel.leave")
    @Subcommand("leave")
    @CommandAlias("channel")
    public class LeaveCommand extends BaseCommand {

        @Default
        public void onLeave(Player player) {
            // get current
            Channel current = plugin.getServerSyncServiceManager().getDataService().getCurrentChannel(player.getUniqueId());

            // if current null
            if (current == null) {
                Messages.getInstance(plugin).generalHelp.message(player);
                return;
            }

            // update current channel (aka remove)
            plugin.getUserManager().use(player.getUniqueId(), (user) -> {
                user.leaveChannel(current);

                // send message
                Messages.getInstance(plugin).channelLeave.message(player, "%channel%", current.getHandle());
            });
        }
    }

    @Subcommand("join")
    @CommandAlias("channel")
    @CommandPermission("space.chat.command.channel.join")
    public class JoinCommand extends BaseCommand {

        @Default
        public void onJoin(Player player, @Single String channel) {
            // get channel
            Channel applicable = plugin.getChannelManager().get(channel, null);
            if (applicable == null) {
                // send message
                Messages.getInstance(plugin).channelInvalid.message(player, "%channel%", channel);
                return;
            }

            // do they have permission?
            if (!player.hasPermission(applicable.getPermission())) {
                Messages.getInstance(plugin).channelAccessDenied.message(player);
                return;
            }

            // set current channel
            plugin.getUserManager().use(player.getUniqueId(), (user) -> {
                user.joinChannel(applicable);

                // send message
                Messages.getInstance(plugin).channelJoin.message(player, "%channel%", channel);
            });
        }
    }

    @Default
    @CatchUnknown
    @HelpCommand
    public void onCommand(Player player) {
        // send help message
        Messages.getInstance(plugin).generalHelp.message(player);
    }
}
