package dev.spaceseries.spacechat.storage.impl.json.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.Channel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChannelGsonDeserializer implements JsonDeserializer<List<Channel>> {

    /**
     * SpaceChat plugin
     */
    private final SpaceChatPlugin plugin;

    /**
     * Channel gson deserializer
     *
     * @param plugin plugin
     */
    public ChannelGsonDeserializer(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on the
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public List<Channel> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = json.getAsJsonArray();

        List<Channel> subscribedChannels = new ArrayList<>();

        // loop through
        array.forEach(element -> {
            // get as string
            String handle = element.getAsString();

            // get channel based on handle
            Channel channel = plugin.getChannelManager().get(handle, null);
            if (channel != null) {
                subscribedChannels.add(channel);
            }
        });

        return subscribedChannels;
    }
}
