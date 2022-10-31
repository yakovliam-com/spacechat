package dev.spaceseries.spacechat.command;

import co.aikar.commands.annotation.*;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.command.SpaceChatCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

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

        plugin.getUserManager().getByName(sender.getName(), user -> {
            String targetName = plugin.getUserManager().getReplyTargetMap().get(sender.getName());
            if(targetName == null){
                Messages.getInstance(plugin).replyNoTarget.message(sender);
                return;
            }

            Player target = Bukkit.getPlayerExact(targetName);

            if(target == null){
                Messages.getInstance(plugin).replyTargetOffline.message(sender, "%target%", targetName);
                return;
            }

            final String messageStr = String.join(" ", args).trim(); //Remove unnecessary spaces

            Component formatSend = Messages.getInstance(plugin).replyFormatSend
                    .compile("%receiver%", targetName, "%message%", messageStr);
            Component formatReceive = Messages.getInstance(plugin).replyFormatReceive
                    .compile("%sender%", sender.getName(), "%message%", messageStr);

            plugin.getUserManager().getReplyTargetMap().put(targetName, sender.getName());

            plugin.getChatManager().sendComponentMessage(formatSend, (Player) sender);
            plugin.getChatManager().sendComponentMessage(formatReceive, Objects.requireNonNull(target));

        });
    }
}
