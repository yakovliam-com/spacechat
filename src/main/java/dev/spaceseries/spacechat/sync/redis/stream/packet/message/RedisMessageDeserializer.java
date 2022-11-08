package dev.spaceseries.spacechat.sync.redis.stream.packet.message;

import com.google.gson.*;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.sync.redis.stream.packet.chat.RedisChatPacket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.lang.reflect.Type;
import java.util.UUID;

public class RedisMessageDeserializer implements JsonDeserializer<RedisMessagePacket> {

    private final SpaceChatPlugin plugin;

    public RedisMessageDeserializer(SpaceChatPlugin plugin){
        this.plugin = plugin;
    }
    @Override
    public RedisMessagePacket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        // get sender uuid
        String senderUUIDString = object.get("senderUUID").getAsString();
        // deserialize
        UUID sender = UUID.fromString(senderUUIDString);

        // get sender name
        String senderName = object.get("senderName").getAsString();

        // get receiver name
        String receiverName = object.get("receiverName").getAsString();

        // get channel string
        String channelStringHandle = object.get("channel") == null ? null : object.get("channel").getAsString();
        // deserialize / get (null = global)
        Channel channel = channelStringHandle == null ? null : plugin.getChannelManager().get(channelStringHandle, null);

        // get server identifier
        String serverIdentifier = object.get("serverIdentifier").getAsString();
        // get server display name
        String serverDisplayName = object.get("serverDisplayName").getAsString();

        // get component string
        String componentString = object.get("component").getAsString();
        // deserialize
        Component component = GsonComponentSerializer.gson().deserialize(componentString);

        // return a new message
        return new RedisMessagePacket(sender, senderName, receiverName, channel, serverIdentifier, serverDisplayName, component);
    }
}
