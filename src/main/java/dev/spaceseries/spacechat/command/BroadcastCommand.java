package dev.spaceseries.spacechat.command;

import dev.spaceseries.spaceapi.command.Command;
import dev.spaceseries.spaceapi.command.Permissible;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
import dev.spaceseries.spaceapi.command.SubCommand;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.serializer.legacy.LegacyComponentSerializer;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.dc.redis.packet.broadcast.RedisBroadcastPacket;
import dev.spaceseries.spacechat.util.chat.ChatUtil;

import java.util.Collections;

import static dev.spaceseries.spacechat.configuration.Config.REDIS_SERVER_IDENTIFIER;

@SubCommand
@Permissible("space.chat.broadcast")
public class BroadcastCommand extends Command {

    public BroadcastCommand() {
        super(SpaceChat.getInstance().getPlugin(), "broadcast", "Broadcast command", Collections.singletonList("bcast"));
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

        // convert to component
        Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

        // send broadcast packet (redis)
        SpaceChat.getInstance().getDynamicConnectionManager().getRedisSupervisor().publishBroadcast(new RedisBroadcastPacket(REDIS_SERVER_IDENTIFIER.get(Config.get()), component));

        // output to game
        ChatUtil.sendComponentMessage(component);
    }
}
