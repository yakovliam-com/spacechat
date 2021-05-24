package dev.spaceseries.spacechat.user;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.model.User;
import dev.spaceseries.spacechat.model.manager.Manager;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UserManager implements Manager {

    private final AsyncLoadingCache<UUID, User> userAsyncCache;

    /**
     * Construct user manager
     */
    public UserManager(SpaceChat plugin) {
        userAsyncCache = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .buildAsync((u) -> plugin.getStorageManager().getCurrent().getUser(u));
    }

    /**
     * Returns user
     *
     * @param uuid uuid
     * @return user
     */
    public CompletableFuture<User> get(UUID uuid) {
        return userAsyncCache.get(uuid);
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
        Bukkit.getScheduler().runTaskAsynchronously(SpaceChat.getInstance(), () -> {
            SpaceChat.getInstance().getStorageManager().getCurrent().updateUser(user);
        });
    }

    /**
     * Gets user by username
     *
     * @param username username
     * @return user
     */
    public CompletableFuture<User> getByName(String username) {
        return CompletableFuture.supplyAsync(() -> SpaceChat.getInstance().getStorageManager().getCurrent().getUser(username));
    }
}
