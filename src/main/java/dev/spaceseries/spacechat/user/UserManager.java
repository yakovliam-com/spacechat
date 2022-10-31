package dev.spaceseries.spacechat.user;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.User;
import dev.spaceseries.spacechat.model.manager.Manager;
import dev.spaceseries.spacechat.storage.StorageManager;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class UserManager implements Manager {

    private final SpaceChatPlugin plugin;
    private final AsyncLoadingCache<UUID, User> userAsyncCache;
    private final StorageManager storageManager;
    private Map<String, String> replyTarget;

    /**
     * Construct user manager
     */
    public UserManager(SpaceChatPlugin plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();

        userAsyncCache = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .buildAsync((u) -> storageManager.getCurrent().getUser(u));
    }

    /**
     * Uses user with a consumer
     *
     * @param uuid     uuid
     * @param consumer consumer
     */
    public void use(UUID uuid, Consumer<User> consumer) {
        userAsyncCache.get(uuid).thenAccept(consumer);
    }

    /**
     * Returns a user
     *
     * @param uuid uuid
     * @return user
     */
    public User get(UUID uuid) {
        return userAsyncCache.get(uuid).join();
    }

    /**
     * Invalidates user
     *
     * @param uuid uuid
     */
    public void invalidate(UUID uuid) {
        userAsyncCache.synchronous().invalidate(uuid);
    }

    /**
     * Update a user
     *
     * @param user user
     */
    public void update(User user) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> storageManager.getCurrent().updateUser(user));
    }

    /**
     * Uses user with a consumer
     *
     * @param username username
     * @param consumer consumer
     */
    public void getByName(String username, Consumer<User> consumer) {
        CompletableFuture.supplyAsync(() -> storageManager.getCurrent().getUser(username)).thenAccept(consumer);
    }
    
    public Map<String, String> getReplyTargetMap(){
        if(replyTarget == null){
            replyTarget = new HashMap<>();
        }
        return replyTarget;
    }

    /*
        Get reply target uuid
     */
    public String getReplyTarget(String sender){
        return replyTarget.get(sender);
    }
    /**
     * Returns all
     *
     * @return all
     */
    public Map<UUID, User> getAll() {
        return userAsyncCache.synchronous().asMap();
    }
}
