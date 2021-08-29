package dev.spaceseries.spacechat.storage.impl.json.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.model.User;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserConfigurateSerializer implements TypeSerializer<User> {

    /**
     * SpaceChat plugin
     */
    private final SpaceChatPlugin plugin;

    /**
     * Gson
     */
    private final Gson gson;

    /**
     * User configurate serializer
     *
     * @param plugin plugin
     */
    public UserConfigurateSerializer(SpaceChatPlugin plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .create();
    }

    /**
     * Deserialize an object (of the correct type) from the given configuration
     * node.
     *
     * @param type the type of return value required
     * @param node the node containing serialized data
     * @return an object
     * @throws SerializationException if the presented data is invalid
     * @since 4.0.0
     */
    @Override
    public User deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String uuidString = node.node("uuid").getString();
        String username = node.node("username").getString();

        Date date = gson.fromJson(node.node("date").getString(), Date.class);

        List<String> subscribedChannelHandles = node.node("subscribedChannels").getList(String.class);

        // map to channels
        List<Channel> subscribedChannels = subscribedChannelHandles.stream()
                .map(handle -> plugin.getChannelManager().get(handle, null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new User(plugin, UUID.fromString(uuidString), username, date, subscribedChannels);
    }

    /**
     * Serialize an object to the given configuration node.
     *
     * @param type the type of the input object
     * @param obj  the object to be serialized
     * @param node the node to write to
     * @throws SerializationException if the object cannot be serialized
     * @since 4.0.0
     */
    @Override
    public void serialize(Type type, @Nullable User obj, ConfigurationNode node) throws SerializationException {
        String uuidString = obj.getUuid().toString();
        node.node("uuid").set(uuidString);

        String username = obj.getUsername();
        node.node("username").set(username);

        node.node("date").set(gson.toJson(obj.getDate()));

        node.node("subscribedChannels").setList(String.class, obj.getSubscribedChannels().stream()
                .map(Channel::getHandle)
                .collect(Collectors.toList()));
    }
}