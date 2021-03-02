package dev.spaceseries.spacechat.dc.redis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.serializer.gson.GsonComponentSerializer;

import java.lang.reflect.Type;

public class RedisChatMessageSerializer implements JsonSerializer<RedisChatMessage> {

    @Override
    public JsonElement serialize(RedisChatMessage src, Type typeOfSrc, JsonSerializationContext context) {
        // create json element
        JsonObject element = new JsonObject();

        // add properties
        element.addProperty("senderUUID", src.getSender().toString());
        element.addProperty("senderName", src.getSenderName());
        element.addProperty("serverIdentifier", src.getServerIdentifier());
        element.addProperty("serverDisplayName", src.getServerDisplayName());
        element.addProperty("component", GsonComponentSerializer.gson().serialize(src.getComponent()));

        return element;
    }
}
