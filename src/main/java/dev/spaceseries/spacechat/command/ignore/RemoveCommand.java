package dev.spaceseries.spacechat.command.ignore;

import dev.spaceseries.spaceapi.command.*;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import org.bukkit.OfflinePlayer;

@SubCommand
@PlayersOnly
@Permissible("space.chat.command.ignore.remove")
public class RemoveCommand extends Command {

    public RemoveCommand() {
        super(SpaceChat.getInstance().getPlugin(), "remove");
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String label, String... args) {
        // args
        if (args.length != 1) {
            Messages.getInstance().generalHelp.msg(sender);
            return;
        }

        // understood this is deprecated, but should be fine as it's just a reference to get a player -- not used as storage
        OfflinePlayer target = SpaceChat.getInstance().getServer().getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore()) {
            Messages.getInstance().playerNotFound.msg(sender);
            return;
        }

        // TODO Implement removing the target from the decided storage method -- also check if the sender even has ignored the target
    }
}
