package dev.spaceseries.spacechat.sync.redis.stream.packet.chat;

import dev.spaceseries.spaceapi.lib.adventure.adventure.text.serializer.gson.GsonComponentSerializer;
import dev.spaceseries.spaceapi.lib.google.gson.JsonElement;
import dev.spaceseries.spaceapi.lib.google.gson.JsonObject;
import dev.spaceseries.spaceapi.lib.google.gson.JsonSerializationContext;
import dev.spaceseries.spaceapi.lib.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class RedisChatPacketSerializer implements JsonSerializer<RedisChatPacket> {

    @Override
    public JsonElement serialize(RedisChatPacket src, Type typeOfSrc, JsonSerializationContext context) {
        // create json element
        JsonObject element = new JsonObject();

        // add properties
        element.addProperty("senderUUID", src.getSender().toString());
        element.addProperty("senderName", src.getSenderName());
        element.addProperty("channel", src.getChannel() == null ? null : src.getChannel().getHandle());
        element.addProperty("serverIdentifier", src.getServerIdentifier());
        element.addProperty("serverDisplayName", src.getServerDisplayName());
        element.addProperty("component", GsonComponentSerializer.gson().serialize(src.getComponent()));

        return element;
    }
}
