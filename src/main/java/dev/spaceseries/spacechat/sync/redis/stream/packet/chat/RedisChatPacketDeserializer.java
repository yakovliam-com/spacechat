package dev.spaceseries.spacechat.sync.redis.stream.packet.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import dev.spaceseries.spaceapi.lib.google.gson.JsonDeserializationContext;
import dev.spaceseries.spaceapi.lib.google.gson.JsonDeserializer;
import dev.spaceseries.spaceapi.lib.google.gson.JsonElement;
import dev.spaceseries.spaceapi.lib.google.gson.JsonObject;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.model.Channel;

import java.lang.reflect.Type;
import java.util.UUID;

public class RedisChatPacketDeserializer implements JsonDeserializer<RedisChatPacket> {

    private final SpaceChat plugin;

    public RedisChatPacketDeserializer(SpaceChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public RedisChatPacket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();

        // get sender uuid
        String senderUUIDString = object.get("senderUUID").getAsString();
        // deserialize
        UUID sender = UUID.fromString(senderUUIDString);

        // get sender name
        String senderName = object.get("senderName").getAsString();

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
        return new RedisChatPacket(sender, senderName, channel, serverIdentifier, serverDisplayName, component);
    }
}
