package dev.spaceseries.spacechat.command.spacechat;

import dev.spaceseries.spaceapi.command.Command;
import dev.spaceseries.spaceapi.command.Permissible;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
import dev.spaceseries.spaceapi.command.SubCommand;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;

import java.util.concurrent.CompletableFuture;

@SubCommand
@Permissible("space.chat.command.reload")
public class ReloadCommand extends Command {

    private final SpaceChat plugin;

    public ReloadCommand(SpaceChat plugin) {
        super(plugin.getPlugin(), "reload");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String s, String... args) {
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
                Messages.getInstance(plugin).reloadFailure.msg(sender);
                e.printStackTrace();
                return;
            }
            Messages.getInstance(plugin).reloadSuccess.msg(sender);
        });
    }
}
