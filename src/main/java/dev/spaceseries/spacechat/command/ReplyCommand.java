package dev.spaceseries.spacechat.command;

import co.aikar.commands.annotation.*;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.command.SpaceChatCommand;
import dev.spaceseries.spacechat.api.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermission("space.chat.command.reply")
@CommandAlias("reply|r")
public class ReplyCommand extends SpaceChatCommand {
    /**
     * SpaceChat command
     *
     * @param plugin plugin
     */
    public ReplyCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @Default
    public void onReply(CommandSender sender, @Split(" ") String[] args){
        if(args.length == 0){
            Messages.getInstance(plugin).replyArgs.message(sender);
            return;
        }
        String targetName = plugin.getUserManager().getReplyTarget(sender.getName());

        // Verify if player has a replier
        if(targetName == null){
            Messages.getInstance(plugin).replyNoTarget.message(sender);
            return;
        }

        // Verify if player is online
        if(!plugin.getUserManager().getOnlinePlayers().contains(targetName)){
            Messages.getInstance(plugin).replyTargetOffline.message(sender, "%target%", targetName);
            return;
        }

        // Construct reply message
        final String messageStr = String.join(" ", args).trim(); //Remove unnecessary spaces


        // messages
        Message formatSend = Messages.getInstance(plugin).replyFormatSend;
        Message formatReceive = Messages.getInstance(plugin).replyFormatReceive;

        // send a message to player in the same server if is connected
        formatReceive.message(Bukkit.getPlayer(targetName),
                "%sender%", sender.getName(),
                "%message%", messageStr);

        // put replier in map
        plugin.getUserManager().getReplyTargetMap().put(targetName, sender.getName());

        // send to player
        plugin.getChatManager().sendPlayerMessage((Player) sender, targetName, messageStr, formatSend, formatReceive);

    }
}
