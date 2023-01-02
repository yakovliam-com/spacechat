package dev.spaceseries.spacechat.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.common.collect.Lists;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import org.bukkit.entity.Player;
import java.util.List;

@CommandPermission("space.chat.command.ignore")
@CommandAlias("ignore")
public class IgnoreCommand extends SpaceChatCommand {

    public IgnoreCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @Subcommand("add")
    @CommandAlias("ignore")
    @CommandPermission("space.chat.command.ignore.add")
    public class AddCommand extends BaseCommand {

        @Default
        public void onAdd(Player player, @Single String targetName) {
            // get user
            plugin.getUserManager().getByName(targetName, (user -> {
                if (user == null) {
                    Messages.getInstance(plugin).playerNotFound.message(player);
                    return;
                }

                plugin.getUserManager().getByName(player.getName(), p -> {
                    if(p.getIgnoredUsers().contains(targetName)){
                        Messages.getInstance(plugin).ignoreAlready.message(player, "%player%", targetName);
                        return;
                    }

                    Messages.getInstance(plugin).ignoreAdded.message(player, "%player%", targetName);
                    plugin.getStorageManager().getCurrent().createIgnoredUser(player.getName(), targetName);
                });
            }));
        }
    }

    @Subcommand("list")
    @CommandAlias("ignore")
    @CommandPermission("space.chat.command.ignore.list")
    public class ListCommand extends BaseCommand {

        @Default
        public void onList(Player player, @Optional String pageStr ) {
            plugin.getUserManager().getByName(player.getName(), user -> {
                if (user == null) {
                    return;
                }

                List<List<String>> players = Lists.partition(user.getIgnoredUsers(), 10);
                int pages = players.size();
                int page = (isNumber(pageStr)) ? Integer.parseInt(pageStr)-1 : 0;

                Integer actualP = page+1;
                Integer maxP = pages;
                Integer nextP = actualP+1;

                if(page > maxP){
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
                    Messages.getInstance(plugin).ignoreListFooter.message(player, "%next-page%", nextP.toString());
                }
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
    }

    @Subcommand("remove")
    @CommandAlias("ignore")
    @CommandPermission("space.chat.command.ignore.remove")
    public class RemoveCommand extends BaseCommand {

        @Default
        public void onRemove(Player player, @Single String targetName) {
            // get user
            plugin.getUserManager().getByName(targetName, (user) -> {
                if (user == null) {
                    Messages.getInstance(plugin).playerNotFound.message(player);
                    return;
                }

                plugin.getUserManager().getByName(player.getName(), p -> {
                    if(!p.getIgnoredUsers().contains(targetName)){
                        Messages.getInstance(plugin).ignoreNotFound.message(player, "%player%", targetName);
                        return;
                    }

                    Messages.getInstance(plugin).ignoreRemoved.message(player, "%player%", targetName);
                    plugin.getStorageManager().getCurrent().deleteIgnoredUser(player.getName(), targetName);
                });
            });
        }
    }

    @Default
    @CatchUnknown
    @HelpCommand
    public void onDefault(Player player) {
        // send help message
        Messages.getInstance(plugin).ignoreHelp.message(player);
    }
}
