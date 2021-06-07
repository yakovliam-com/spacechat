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
                // reload configurations
                plugin.loadConfigs();

                // reload formats
                plugin.loadFormats();

                // reload channels
                plugin.loadChannels();

                // reload storage
                plugin.loadStorage();

                // load messages
                plugin.loadMessages();

                // load dynamic connections
                plugin.loadSyncServices();
            } catch (Exception e) {
                Messages.getInstance(plugin).reloadFailure.msg(sender);
                e.printStackTrace();
                return;
            }
            Messages.getInstance(plugin).reloadSuccess.msg(sender);
        });
    }
}
