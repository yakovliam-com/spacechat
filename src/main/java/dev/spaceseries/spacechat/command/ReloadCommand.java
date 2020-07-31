package dev.spaceseries.spacechat.command;

import dev.spaceseries.api.command.Command;
import dev.spaceseries.api.command.Permissible;
import dev.spaceseries.api.command.SpaceCommandSender;
import dev.spaceseries.api.command.SubCommand;
import dev.spaceseries.spacechat.SpaceChat;

import java.util.concurrent.CompletableFuture;

import static dev.spaceseries.spacechat.Messages.RELOAD_FAILURE;
import static dev.spaceseries.spacechat.Messages.RELOAD_SUCCESS;

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
            } catch (Exception e) {
                RELOAD_FAILURE.msg(sender);
                e.printStackTrace();
                return;
            }
            RELOAD_SUCCESS.msg(sender);
        });
    }
}
