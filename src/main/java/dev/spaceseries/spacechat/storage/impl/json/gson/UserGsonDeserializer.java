package dev.spaceseries.spacechat.storage.impl.json.gson;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.model.User;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserGsonDeserializer implements JsonDeserializer<User> {

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
     * User gson deserializer
     *
     * @param plugin plugin
     */
    public UserGsonDeserializer(SpaceChatPlugin plugin) {
        this.plugin = plugin;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(listType, new ChannelGsonDeserializer(plugin))
                .registerTypeAdapter(listType, new ChannelGsonSerializer(plugin))
                .create();
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
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String uuidString = object.get("uuid").getAsString();
        String username = object.get("username").getAsString();
        String dateString = object.get("date").getAsString();
        JsonArray subscribedChannelsArray = object.get("subscribedChannels").getAsJsonArray();

        Date date = gson.fromJson(dateString, Date.class);
        List<Channel> subscribedChannels = gson.fromJson(subscribedChannelsArray, this.listType);

        return new User(plugin, UUID.fromString(uuidString), username, date, subscribedChannels);
    }
}