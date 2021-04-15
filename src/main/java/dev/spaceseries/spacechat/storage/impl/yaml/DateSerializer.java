package dev.spaceseries.spacechat.storage.impl.yaml;

import dev.spaceseries.spaceapi.lib.google.gson.JsonElement;
import dev.spaceseries.spaceapi.lib.google.gson.JsonPrimitive;
import dev.spaceseries.spaceapi.lib.google.gson.JsonSerializationContext;
import dev.spaceseries.spaceapi.lib.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

public class DateSerializer implements JsonSerializer<Date> {

    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.getTime());
    }
}