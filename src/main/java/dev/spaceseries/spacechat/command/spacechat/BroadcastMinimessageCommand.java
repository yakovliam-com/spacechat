package dev.spaceseries.spacechat.command.spacechat;

import dev.spaceseries.spaceapi.command.Command;
import dev.spaceseries.spaceapi.command.Permissible;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
import dev.spaceseries.spaceapi.command.SubCommand;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.minimessage.MiniMessage;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.messaging.redis.packet.broadcast.RedisBroadcastPacket;
import dev.spaceseries.spacechat.util.chat.ChatUtil;

import java.util.Collections;

import static dev.spaceseries.spacechat.config.Config.BROADCAST_USE_LANG_WRAPPER;
import static dev.spaceseries.spacechat.config.Config.REDIS_SERVER_IDENTIFIER;

@SubCommand
@Permissible("space.chat.command.broadcastminimessage")
public class BroadcastMinimessageCommand extends Command {

    public BroadcastMinimessageCommand() {
        super(SpaceChat.getInstance().getPlugin(), "broadcastminimessage", "Broadcast (minimessage) command", Collections.singletonList("bcastm"));
    }

    @Override
    public void onCommand(SpaceCommandSender sender, String label, String... args) {
        // if message length is not long enough
        if (args.length <= 0) {
            Messages.getInstance().broadcastArgs.msg(sender);
            return;
        }

        // compile args into single message
        String message = String.join(" ", args);

        // parse through minimessage
        Component component = MiniMessage.get().deserialize(message);

        // use lang wrapper?
        if (BROADCAST_USE_LANG_WRAPPER.get(Config.get())) {
            Component previousComponent = component;
            component = Messages.getInstance().broadcastWrapper.toComponent()
                    .replaceText((b) -> b.match("%message%")
                            .replacement(previousComponent));
        }

        // send broadcast packet (redis)
        SpaceChat.getInstance().getMessagingService().getSupervisor().publishBroadcast(new RedisBroadcastPacket(REDIS_SERVER_IDENTIFIER.get(Config.get()), component));

        // output to game
        ChatUtil.sendComponentMessage(component);
    }
}
