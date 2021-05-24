package dev.spaceseries.spacechat.sync.redis.data;

import dev.spaceseries.spaceapi.lib.redis.jedis.Jedis;
import dev.spaceseries.spaceapi.lib.redis.jedis.JedisPool;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.sync.ServerDataSyncService;
import dev.spaceseries.spacechat.sync.ServerSyncServiceManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static dev.spaceseries.spacechat.config.Config.*;

public class RedisServerDataSyncService extends ServerDataSyncService {

    /**
     * Pool
     */
    private final JedisPool pool;

    /**
     * Construct server sync service
     *
     * @param serviceManager service manager
     */
    public RedisServerDataSyncService(ServerSyncServiceManager serviceManager) {
        super(serviceManager);

        // initialize pool
        this.pool = this.getServiceManager().getRedisProvider().provide();
    }

    /**
     * Subscribes a player to a channel
     *
     * @param uuid    uuid
     * @param channel channel
     */
    @Override
    public void subscribeToChannel(UUID uuid, Channel channel) {
        try (Jedis jedis = pool.getResource()) {
            // update

            // lpush new channel
            jedis.lpush(REDIS_PLAYER_SUBSCRIBED_CHANNELS_LIST_KEY.get(Config.get())
                    .replace("%uuid%", uuid.toString()), channel.getHandle());

            // also lpush to master channels list that contains a list of uuids for every player subscribed to that given channel
            jedis.lpush(REDIS_CHANNELS_SUBSCRIBED_UUIDS_LIST_KEY.get(Config.get())
                    .replace("%channel%", channel.getHandle()), uuid.toString());
        }
    }

    /**
     * Unsubscribes a player from a channel
     *
     * @param uuid              uuid
     * @param subscribedChannel subscribed channel
     */
    @Override
    public void unsubscribeFromChannel(UUID uuid, Channel subscribedChannel) {
        try (Jedis jedis = pool.getResource()) {
            // update

            // lrem channel
            jedis.lrem(REDIS_PLAYER_SUBSCRIBED_CHANNELS_LIST_KEY.get(Config.get())
                    .replace("%uuid%", uuid.toString()), 0, subscribedChannel.getHandle());

            // also lrem from master channels list that contains a list of uuids for every player subscribed to that given channel
            jedis.lrem(REDIS_CHANNELS_SUBSCRIBED_UUIDS_LIST_KEY.get(Config.get())
                    .replace("%channel%", subscribedChannel.getHandle()), 0, uuid.toString());
        }
    }

    /**
     * Updates the current channel that a player is talking in
     *
     * @param uuid    uuid
     * @param channel channel
     */
    @Override
    public void updateCurrentChannel(UUID uuid, Channel channel) {
        try (Jedis jedis = pool.getResource()) {
            // update key
            jedis.set(REDIS_PLAYER_CURRENT_CHANNEL_KEY.get(Config.get())
                    .replace("%uuid%", uuid.toString()), channel.getHandle());
        }
    }

    /**
     * Gets a list of subscribed channels
     *
     * @param uuid uuid
     * @return channels
     */
    @Override
    public List<Channel> getSubscribedChannels(UUID uuid) {
        try (Jedis jedis = pool.getResource()) {
            // get
            return jedis.lrange(REDIS_PLAYER_SUBSCRIBED_CHANNELS_LIST_KEY.get(Config.get())
                    .replace("%uuid%", uuid.toString()), 0, -1).stream()
                    .map(s -> SpaceChat.getInstance().getChannelManager().get(s, null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Gets the current channel that a player is talking in
     *
     * @param uuid uuid
     * @return channel
     */
    @Override
    public Channel getCurrentChannel(UUID uuid) {
        try (Jedis jedis = pool.getResource()) {
            // get channel
            return SpaceChat.getInstance().getChannelManager().get(jedis.get(REDIS_PLAYER_CURRENT_CHANNEL_KEY.get(Config.get())
                    .replace("%uuid%", uuid.toString())), null);
        }
    }

    /**
     * Gets a list of all of the uuids of players who are currently subscribed to a given channel
     * <p>
     * This only includes the uuids of players who are currently online
     *
     * @param channel channel
     * @return uuids
     */
    @Override
    public List<UUID> getSubscribedUUIDs(Channel channel) {
        try (Jedis jedis = pool.getResource()) {
            // get list of uuids of players who've subscribed to a given channel
            List<String> uuids = jedis.lrange(REDIS_CHANNELS_SUBSCRIBED_UUIDS_LIST_KEY.get(Config.get())
                    .replace("%channel%", channel.getHandle()), 0, -1);

            // map and return
            return uuids.stream()
                    .map(UUID::fromString)
                    .filter(u -> {
                        Player p = Bukkit.getPlayer(u);
                        return p != null && p.isOnline();
                    })
                    .collect(Collectors.toList());
        }
    }
}
