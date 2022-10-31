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

    @CommandCompletion("@playerList @nothing")
    @Default
    public void onMessage(CommandSender sender, @Split(" ") String[] args){
        if(args.length <= 1){
            Messages.getInstance(plugin).messageArgs.message(sender);
            return;
        }
        final String targetName = args[0];

        if(sender.getName().equalsIgnoreCase(targetName)){
            Messages.getInstance(plugin).messageSelf.message(sender);
            return;
        }

        final String messageStr = String.join(" ", args)
                .replace(targetName, "") //Replace target to empty
                .trim(); //Remove unnecessary spaces

        //DEBUG
        System.out.println(plugin.getUserManager().getAll());
        //DEBUG

        Component formatSend = Messages.getInstance(plugin).messageFormatSend
                .compile("%receiver%", targetName, "%message%", messageStr);
        Component formatReceive = Messages.getInstance(plugin).messageFormatReceive
                .compile("%sender%", sender.getName(), "%message%", messageStr);

        // get user
        plugin.getUserManager().getByName(targetName, user -> {
            if (user == null) {
                System.out.println(1);
                Messages.getInstance(plugin).messagePlayerNotFound.message(sender);
                return;
            }
            Player target = Bukkit.getPlayerExact(targetName);

            plugin.getUserManager().getReplyTargetMap().put(targetName, sender.getName());
            System.out.println(plugin.getUserManager().getReplyTargetMap());
            plugin.getChatManager().sendComponentMessage(formatSend, (Player) sender);
            plugin.getChatManager().sendComponentMessage(formatReceive, Objects.requireNonNull(target));

        });
    }


}
