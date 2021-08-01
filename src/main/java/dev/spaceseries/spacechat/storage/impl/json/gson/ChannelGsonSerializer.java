package dev.spaceseries.spacechat.storage.impl.json.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.Channel;

import java.lang.reflect.Type;
import java.util.List;

public class ChannelGsonSerializer implements JsonSerializer<List<Channel>> {

    /**
     * SpaceChat plugin
     */
    private final SpaceChatPlugin plugin;

    /**
     * Channel gson deserializer
     *
     * @param plugin plugin
     */
    public ChannelGsonSerializer(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gson invokes this call-back method during serialization when it encounters a field of the
     * specified type.
     *
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonSerializationContext#serialize(Object, Type)} method to create JsonElements for any
     * non-trivial field of the {@code src} object. However, you should never invoke it on the
     * {@code src} object itself since that will cause an infinite loop (Gson will call your
     * call-back method again).</p>
     *
     * @param src       the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context
     * @return a JsonElement corresponding to the specified object.
     */
    @Override
    public JsonElement serialize(List<Channel> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();

        // loop and add to json array
        src.forEach(channel -> {
            array.add(channel.getHandle());
        });

        return array;
    }
}
