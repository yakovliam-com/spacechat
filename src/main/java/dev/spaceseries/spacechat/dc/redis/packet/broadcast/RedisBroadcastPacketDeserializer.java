package dev.spaceseries.spacechat.dc.redis.packet.broadcast;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.serializer.gson.GsonComponentSerializer;
import dev.spaceseries.spacechat.dc.redis.packet.chat.RedisChatPacket;

import java.lang.reflect.Type;
import java.util.UUID;

public class RedisBroadcastPacketDeserializer implements JsonDeserializer<RedisBroadcastPacket> {

    @Override
    public RedisBroadcastPacket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();

        // get server identifier
        String serverIdentifier = object.get("serverIdentifier").getAsString();

        // get component string
        String componentString = object.get("component").getAsString();
        // deserialize
        Component component = GsonComponentSerializer.gson().deserialize(componentString);

        // return a new message
        return new RedisBroadcastPacket(serverIdentifier, component);
    }
}
