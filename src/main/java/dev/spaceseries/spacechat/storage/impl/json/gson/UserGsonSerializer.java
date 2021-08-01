package dev.spaceseries.spacechat.storage.impl.json.gson;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.model.User;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class UserGsonSerializer implements JsonSerializer<User> {

    /**
     * SpaceChat plugin
     */
    private final SpaceChatPlugin plugin;

    /**
     * Gson
     */
    private final Gson gson;

    /**
     * List type token
     */
    private final Type listType = new TypeToken<List<Channel>>() {
    }.getType();

    /**
     * User gson serializer
     *
     * @param plugin plugin
     */
    public UserGsonSerializer(SpaceChatPlugin plugin) {
        this.plugin = plugin;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(listType, new ChannelGsonDeserializer(plugin))
                .registerTypeAdapter(listType, new ChannelGsonSerializer(plugin))
                .create();
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
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject userObject = new JsonObject();

        String uuidString = src.getUuid().toString();
        userObject.addProperty("uuid", uuidString);

        String username = src.getUsername();
        userObject.addProperty("username", username);

        String dateString = gson.toJson(src.getDate());
        userObject.addProperty("date", dateString);

        JsonArray subscribedChannelsArray = new JsonParser().parse(gson.toJson(src.getSubscribedChannels(), listType)).getAsJsonArray();
        userObject.add("subscribedChannels", subscribedChannelsArray);

        return userObject;
    }
}