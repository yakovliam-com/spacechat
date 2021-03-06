package dev.spaceseries.spacechat.sync.redis.stream.packet.broadcast;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class RedisBroadcastPacketSerializer implements JsonSerializer<RedisBroadcastPacket> {

    @Override
    public JsonElement serialize(RedisBroadcastPacket src, Type typeOfSrc, JsonSerializationContext context) {
        // create json element
        JsonObject element = new JsonObject();

        // add properties
        element.addProperty("serverIdentifier", src.getServerIdentifier());
        element.addProperty("component", GsonComponentSerializer.gson().serialize(src.getComponent()));

        return element;
    }
}
