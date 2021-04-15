package dev.spaceseries.spacechat.storage.impl.yaml;

import dev.spaceseries.spaceapi.lib.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return new Date(jsonElement.getAsLong());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}