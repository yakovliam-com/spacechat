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

    public ReloadCommand() {
        super(SpaceChat.getInstance().getPlugin(), "reload");
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String s, String... args) {
        // run async task
        CompletableFuture.runAsync(() -> {
            try {
                // reload configurations
                SpaceChat.getInstance().loadConfigs();

                // reload formats
                SpaceChat.getInstance().loadFormats();

                // reload channels
                SpaceChat.getInstance().loadChannels();

                // reload storage
                SpaceChat.getInstance().loadStorage();

                // load messages
                SpaceChat.getInstance().loadMessages();

                // load dynamic connections
                SpaceChat.getInstance().loadSyncServices();
            } catch (Exception e) {
                Messages.getInstance().reloadFailure.msg(sender);
                e.printStackTrace();
                return;
            }
            Messages.getInstance().reloadSuccess.msg(sender);
        });
    }
}
