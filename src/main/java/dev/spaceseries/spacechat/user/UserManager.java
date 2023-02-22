package dev.spaceseries.spacechat.user;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.config.SpaceChatConfigKeys;
import dev.spaceseries.spacechat.model.User;
import dev.spaceseries.spacechat.model.manager.Manager;
import dev.spaceseries.spacechat.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class UserManager implements Manager {

    private final SpaceChatPlugin plugin;
    private final AsyncLoadingCache<UUID, User> userAsyncCache;
    private final StorageManager storageManager;
    private final Map<String, String> replyTarget = new HashMap<>();

    private final Map<String, List<String>> ignoredList = new HashMap<>();
    private boolean onUpdate = false;
    private Map<String, String> onlinePlayers = new HashMap<>();
    private final Map<String, String> cachedOnlinePlayers = new ConcurrentHashMap<>();

    /**
     * Construct user manager
     */
    public UserManager(SpaceChatPlugin plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
        userAsyncCache = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .buildAsync((u) -> storageManager.getCurrent().getUser(u));
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (onUpdate) {
                return;
            }
            onUpdate = true;
            try {
                if (!Bukkit.getOnlinePlayers().isEmpty()) {
                    final String id = SpaceChatConfigKeys.REDIS_SERVER_IDENTIFIER.get(plugin.getSpaceChatConfig().getAdapter());
                    final List<String> players = new ArrayList<>();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    setOnlinePlayers(id, players);
                    plugin.getServerSyncServiceManager().getStreamService().publishPlayerList(id, players);
                }
                onlinePlayers = new HashMap<>(cachedOnlinePlayers);
                cachedOnlinePlayers.clear();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            onUpdate = false;
        }, 200L, 80L);
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

    /**
     * Get reply target map
     *
     * @return Reply Target Map
     */
    public Map<String, String> getReplyTargetMap() {
        return replyTarget;
    }


    /**
     * Get ignore list map
     *
     * @return ignore list map
     */
    public Map<String, List<String>> getIgnoredList() {
        return ignoredList;
    }

    /**
     * Get ignore list for user
     *
     * @return ignore list for user
     */
    public List<String> getIgnoredList(String name) {
        return ignoredList.getOrDefault(name, List.of());
    }


    /**
        Get reply target uuid
     */
    public String getReplyTarget(String sender){
        return replyTarget.get(sender);
    }

    /**
     * Get current online players separated by server identifiers
     *
     * @return A multimap with player names.
     */
    public Map<String, String> getOnlinePlayers() {
        return onlinePlayers;
    }

    public Map<String, List<String>> getOnlinePlayersByServer() {
        if (onlinePlayers.isEmpty()) {
            return new HashMap<>();
        }
        final Map<String, List<String>> map = new HashMap<>();
        for (var entry : onlinePlayers.entrySet()) {
            map.computeIfAbsent(entry.getValue(), s -> new ArrayList<>()).add(entry.getKey());
        }
        return map;
    }

    /**
     * Returns all
     *
     * @return all
     */
    public Map<UUID, User> getAll() {
        return userAsyncCache.synchronous().asMap();
    }

    public void setOnlinePlayers(String id, List<String> players) {
        for (String player : players) {
            cachedOnlinePlayers.put(player, id);
        }
    }

    public boolean isPlayerOnline(String name) {
        return name.equalsIgnoreCase("@console") || onlinePlayers.containsKey(name);
    }
}
