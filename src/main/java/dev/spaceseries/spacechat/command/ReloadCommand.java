package dev.spaceseries.spacechat.command;

import dev.spaceseries.api.command.Command;
import dev.spaceseries.api.command.Permissible;
import dev.spaceseries.api.command.SpaceCommandSender;
import dev.spaceseries.api.command.SubCommand;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;

import java.util.concurrent.CompletableFuture;

@SubCommand
@Permissible("space.chat.reload")
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

                // reload storage
                SpaceChat.getInstance().loadStorage();

                // load messages
                SpaceChat.getInstance().loadMessages();

            } catch (Exception e) {
                Messages.getInstance().RELOAD_FAILURE.msg(sender);
                e.printStackTrace();
                return;
            }
            Messages.getInstance().RELOAD_SUCCESS.msg(sender);
        });
    }
}
