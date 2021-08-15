package dev.spaceseries.spacechat.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

@CommandAlias("spacechat")
@CommandPermission("space.chat.command")
public class SpaceChatCommand extends dev.spaceseries.spacechat.api.command.SpaceChatCommand {

    public SpaceChatCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @CommandAlias("spacechat")
    @Subcommand("reload")
    @CommandPermission("space.chat.command.reload")
    public class ReloadCommand extends BaseCommand {

        @Default
        public void onReload(CommandSender sender) {
            // run async task
            CompletableFuture.runAsync(() -> {
                try {
                    // load configs
                    plugin.loadConfigs();
                    // load formats
                    plugin.loadFormats();
                    // load chat manager
                    plugin.loadChatManager();
                    // load connection managers
                    plugin.loadSyncServices();

                    // initialize sync services, since they have to be instantiated and used after the chat manager
                    // is created
                    plugin.getChatManager().initSyncServices();

                    // load channels
                    plugin.loadChannels();

                    // load users
                    plugin.loadUsers();

                    // renew messages
                    Messages.renew();

                } catch (Exception e) {
                    Messages.getInstance(plugin).reloadFailure.message(sender);
                    e.printStackTrace();
                    return;
                }
                Messages.getInstance(plugin).reloadSuccess.message(sender);
            });
        }
    }

    @Default
    @CatchUnknown
    @HelpCommand
    public void onDefault(CommandSender sender) {
        // send help message
        Messages.getInstance(plugin).generalHelp.message(sender);
    }
}
