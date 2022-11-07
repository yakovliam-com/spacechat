package dev.spaceseries.spacechat.command;

import co.aikar.commands.annotation.*;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.command.SpaceChatCommand;
import dev.spaceseries.spacechat.api.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@CommandPermission("space.chat.command.message")
@CommandAlias("message|msg|m|tell")
public class MessageCommand extends SpaceChatCommand {

    /**
     * SpaceChat command
     *
     * @param plugin plugin
     */
    public MessageCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @Default
    public void onMessage(CommandSender sender, @Split(" ") String[] args) {
        if (args.length <= 1) {
            Messages.getInstance(plugin).messageArgs.message(sender);
            return;
        }
        final String targetName = args[0];

        if (sender.getName().equalsIgnoreCase(targetName)) {
            Messages.getInstance(plugin).messageSelf.message(sender);
            return;
        }

        // Verify if player exist
        if(!plugin.getUserManager().getOnlinePlayers().contains(targetName)){
            Messages.getInstance(plugin).messagePlayerNotFound.message(sender);
            return;
        }

        // Construct the message
        final String messageStr = String.join(" ", args)
                .replace(targetName, "") //Replace target to empty
                .trim(); //Remove unnecessary spaces

        // put replier in map
        plugin.getUserManager().getReplyTargetMap().put(targetName, sender.getName());

        // messages
        Message formatSend = Messages.getInstance(plugin).messageFormatSend;
        Message formatReceive = Messages.getInstance(plugin).messageFormatReceive;

        // send a message to player in the same server if is connected
        formatReceive.message(Bukkit.getPlayer(targetName),
                "%sender%", sender.getName(),
                "%message%", messageStr);

        // send to player
        plugin.getChatManager().sendPlayerMessage((Player) sender, targetName, messageStr, formatSend, formatReceive);

    }


}
