package dev.spaceseries.spacechat.command;

import co.aikar.commands.annotation.*;
import com.google.common.collect.Lists;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@CommandPermission("space.chat.command.ignore")
@CommandAlias("ignore")
public class IgnoreCommand extends dev.spaceseries.spacechat.api.command.SpaceChatCommand {

    public IgnoreCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }


    @Subcommand("add")
    @CommandCompletion("@chatplayers")
    @CommandPermission("space.chat.command.ignore.add")
    @Syntax(" <playerName>")
    public void onAdd(Player player, @Single String targetName) {

        // self ignore LOL
        if(player.getName().equalsIgnoreCase(targetName)){
            Messages.getInstance(plugin).selfIgnore.message(player);
            return;
        }

        if (!plugin.getUserManager().isPlayerLoaded(player.getName())) {
            Messages.getInstance(plugin).ignoreNotLoaded.message(player);
            return;
        }

        // get user
        plugin.getUserManager().getByName(targetName, (user -> {
            if (user == null) {
                Messages.getInstance(plugin).playerNotFound.message(player);
                return;
            }

            plugin.getUserManager().getByName(player.getName(), p -> {
                if(p.isIgnored(targetName)){
                    Messages.getInstance(plugin).ignoreAlready.message(player, "%player%", targetName);
                    return;
                }

                Messages.getInstance(plugin).ignoreAdded.message(player, "%player%", targetName);
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    plugin.getStorageManager().getCurrent().createIgnoredUser(player.getName(), targetName);
                    p.getIgnoredUsers().add(targetName);
                });

            });
        }));
    }

    @Subcommand("list")
    @CommandPermission("space.chat.command.ignore.list")
    @Syntax(" [page]")
    public void onList(Player player, @Optional String pageStr ) {
        if (!plugin.getUserManager().isPlayerLoaded(player.getName())) {
            Messages.getInstance(plugin).ignoreNotLoaded.message(player);
            return;
        }
        plugin.getUserManager().getByName(player.getName(), user -> {
            if (user == null) {
                return;
            }

            List<List<String>> players = Lists.partition(user.getIgnoredUsers(), 10);
            int pages = players.size();
            int page = (isNumber(pageStr)) ? Integer.parseInt(pageStr) - 1 : 0;

            Integer actualP = page + 1;
            Integer maxP = pages;
            Integer nextP = actualP + 1;

            if(page > maxP || actualP > pages || page < 0){
                Messages.getInstance(plugin).ignorePageNotFound.message(player);
                return;
            }

            if(players.size() < 1){
                Messages.getInstance(plugin).ignorePageEmpty.message(player);
                return;
            }

            //Header
            Messages.getInstance(plugin).ignoreListHeader.message(player,
                    "%actual-page%", actualP.toString(),
                    "%max-page%", maxP.toString());

            //Format
            for(String ignored : players.get(page)){
                Messages.getInstance(plugin).ignoreListFormat.message(player, "%player%", ignored);
            }

            if(nextP <= maxP){
                //Footer
                Messages.getInstance(plugin).ignoreListFooter.message(player,
                        "%next-page%", nextP.toString());
            }
        });
    }

    @Subcommand("remove")
    @CommandCompletion("@ignoredplayers")
    @CommandPermission("space.chat.command.ignore.remove")
    @Syntax(" <playerName>")
    public void onRemove(Player player, @Single String targetName) {
        if (!plugin.getUserManager().isPlayerLoaded(player.getName())) {
            Messages.getInstance(plugin).ignoreNotLoaded.message(player);
            return;
        }
        // get user
        plugin.getUserManager().getByName(targetName, (user) -> {
            if (user == null) {
                Messages.getInstance(plugin).playerNotFound.message(player);
                return;
            }

            plugin.getUserManager().getByName(player.getName(), p -> {
                if(!p.isIgnored(targetName)){
                    Messages.getInstance(plugin).ignoreNotFound.message(player, "%player%", targetName);
                    return;
                }

                Messages.getInstance(plugin).ignoreRemoved.message(player, "%player%", targetName);

                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    plugin.getStorageManager().getCurrent().deleteIgnoredUser(player.getName(), targetName);
                    p.getIgnoredUsers().remove(targetName);
                });
            });
        });
    }

    private boolean isNumber(String value){
        try{
            Integer.parseInt(value);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    @Default
    @CatchUnknown
    @HelpCommand
    public void onDefault(Player player) {
        // send help message
        Messages.getInstance(plugin).ignoreHelp.message(player);
    }
}
