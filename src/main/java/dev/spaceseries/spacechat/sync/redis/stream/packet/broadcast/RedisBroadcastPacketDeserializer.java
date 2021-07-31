package dev.spaceseries.spacechat.sync.redis.stream.packet.broadcast;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
