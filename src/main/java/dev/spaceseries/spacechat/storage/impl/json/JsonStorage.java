package dev.spaceseries.spacechat.storage.impl.json;

import com.google.gson.JsonObject;
import com.mysql.cj.log.Log;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.model.User;
import dev.spaceseries.spacechat.storage.Storage;
import dev.spaceseries.spacechat.storage.StorageInitializationException;
import org.bukkit.Bukkit;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JsonStorage extends Storage {

    /**
     * Configurate provider
     */
    private final JsonConfigurateProvider jsonConfigurateProvider;

    public JsonStorage(SpaceChatPlugin plugin) {
        super(plugin);
        this.jsonConfigurateProvider = new JsonConfigurateProvider(plugin);
    }

    /**
     * Initializes the storage medium
     */
    @Override
    public void init() throws StorageInitializationException {
        jsonConfigurateProvider.init();
    }

    /**
     * Logs to the storage medium
     *
     * @param data  The data to log
     * @param async async
     */
    @Override
    public void log(LogWrapper data, boolean async) {
        BasicConfigurationNode logs = jsonConfigurateProvider.provideRoot().node("logs");
        Runnable task = () -> {
            try {
                List<LogWrapper> list = logs.getList(LogWrapper.class);
                list.add(data);

                logs.setList(LogWrapper.class, list);
                jsonConfigurateProvider.save(logs);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        };

        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    /**
     * Gets a user
     *
     * @param uuid uuid
     * @return user
     */
    @Override
    public User getUser(UUID uuid) {
        // TODO finish
        BasicConfigurationNode users = jsonConfigurateProvider.provideRoot().node("users");

        // get users
        try {
            List<JsonObject> userObjectList = users.getList(JsonObject.class);
            // find user
            JsonObject userObject = findUserByUUID(userObjectList, uuid);

            if (userObject == null) {
                // since it's null, create it
                User user = new User(plugin, uuid, null, Date.from(Instant.now()), new ArrayList<>());

                // serialize and add to list
                userObjectList.add(null);
                users.setList(JsonObject.class, userObjectList);

                jsonConfigurateProvider.save(users);

                // return
                return user;
            } else {
                // deserialize and return
                return null;
            }

        } catch (SerializationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Finds a user object by their uuid
     *
     * @param jsonObjects json objects
     * @param uuid        uuid
     * @return json object
     */
    private JsonObject findUserByUUID(List<JsonObject> jsonObjects, UUID uuid) {
        return jsonObjects.stream()
                .filter(object -> object.get("uuid").getAsString().equals(uuid.toString()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a user by their username
     *
     * @param username username
     * @return user
     */
    @Override
    public User getUser(String username) {
        return null;
    }

    /**
     * Updates a user
     *
     * @param user user
     */
    @Override
    public void updateUser(User user) {

    }

    /**
     * Closes the storage medium
     */
    @Override
    public void close() {

    }
}
