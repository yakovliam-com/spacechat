package dev.spaceseries.spacechat.command.channel;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.command.SpaceChatCommand;
import dev.spaceseries.spacechat.model.Channel;
import org.bukkit.entity.Player;

@CommandPermission("space.chat.command.channel.leave")
@Subcommand("leave")
@CommandAlias("channel")
public class LeaveCommand extends SpaceChatCommand {

    public LeaveCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @Default
    public void onLeave(Player player, @Single String channel) {
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