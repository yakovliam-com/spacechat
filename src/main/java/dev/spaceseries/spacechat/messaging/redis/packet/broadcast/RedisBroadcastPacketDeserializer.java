package dev.spaceseries.spacechat.messaging.redis.packet.broadcast;

import dev.spaceseries.spaceapi.lib.google.gson.JsonDeserializationContext;
import dev.spaceseries.spaceapi.lib.google.gson.JsonDeserializer;
import dev.spaceseries.spaceapi.lib.google.gson.JsonElement;
import dev.spaceseries.spaceapi.lib.google.gson.JsonObject;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.serializer.gson.GsonComponentSerializer;

import java.lang.reflect.Type;

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
