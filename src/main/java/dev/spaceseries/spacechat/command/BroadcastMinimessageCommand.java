package dev.spaceseries.spacechat.command;

import dev.spaceseries.spaceapi.command.Command;
import dev.spaceseries.spaceapi.command.Permissible;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.sync.redis.stream.packet.broadcast.RedisBroadcastPacket;

import java.util.Arrays;
import java.util.Collections;

import static dev.spaceseries.spacechat.config.SpaceChatConfigKeys.BROADCAST_USE_LANG_WRAPPER;
import static dev.spaceseries.spacechat.config.SpaceChatConfigKeys.REDIS_SERVER_IDENTIFIER;

@Permissible("space.chat.command.broadcastminimessage")
public class BroadcastMinimessageCommand extends Command {

    private final SpaceChat plugin;

    public BroadcastMinimessageCommand(SpaceChat plugin) {
        super(plugin.getPlugin(), "broadcastminimessage", "Broadcast (minimessage) command", Arrays.asList("scbcastm", "bcastm"));
        this.plugin = plugin;
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String label, String... args) {
        // if message length is not long enough
        if (args.length <= 0) {
            Messages.getInstance(plugin).broadcastArgs.msg(sender);
            return;
        }

        // compile args into single message
        String message = String.join(" ", args);

        // parse through minimessage
        Component component = MiniMessage.get().deserialize(message);

        // use lang wrapper?
        if (BROADCAST_USE_LANG_WRAPPER.get(plugin.getSpaceChatConfig().getAdapter())) {
            Component previousComponent = component;
            component = Messages.getInstance(plugin).broadcastWrapper.toComponent()
                    .replaceText((b) -> b.match("%message%")
                            .replacement(previousComponent));
        }

        // send broadcast packet (redis)
        plugin.getServerSyncServiceManager().getStreamService().publishBroadcast(new RedisBroadcastPacket(REDIS_SERVER_IDENTIFIER.get(plugin.getSpaceChatConfig().getAdapter()), component));

        // output to game
        plugin.getChatManager().sendComponentMessage(component);
    }
}
