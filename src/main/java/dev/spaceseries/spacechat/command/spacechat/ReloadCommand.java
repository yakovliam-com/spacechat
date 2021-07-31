package dev.spaceseries.spacechat.command.spacechat;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.command.SpaceChatCommand;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

@CommandAlias("spacechat")
@Subcommand("reload")
@CommandPermission("space.chat.command.reload")
public class ReloadCommand extends SpaceChatCommand {

    public ReloadCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

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

                // close the storage connection pool (if applicable)
                if (plugin.getStorageManager() != null && plugin.getStorageManager().getCurrent() != null) {
                    plugin.getStorageManager().getCurrent().close();
                }

                // load storage
                plugin.loadStorage();
                // load users
                plugin.loadUsers();

            } catch (Exception e) {
                Messages.getInstance(plugin).reloadFailure.message(sender);
                e.printStackTrace();
                return;
            }
            Messages.getInstance(plugin).reloadSuccess.message(sender);
        });
    }
}
