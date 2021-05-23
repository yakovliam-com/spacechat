package dev.spaceseries.spacechat.sync.redis.data;

import dev.spaceseries.spaceapi.lib.redis.jedis.Jedis;
import dev.spaceseries.spaceapi.lib.redis.jedis.JedisPool;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.sync.ServerDataSyncService;
import dev.spaceseries.spacechat.sync.ServerSyncServiceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static dev.spaceseries.spacechat.config.Config.REDIS_PLAYER_CURRENT_CHANNEL_KEY;
import static dev.spaceseries.spacechat.config.Config.REDIS_PLAYER_SUBSCRIBED_CHANNELS_LIST_KEY;

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
     * Updates the list of subscribed channels
     *
     * @param uuid       uuid
     * @param subscribed subscribed
     */
    @Override
    public void updateSubscribedChannels(UUID uuid, List<Channel> subscribed) {
        try (Jedis jedis = pool.getResource()) {
            // update

            // delete key
            jedis.del(REDIS_PLAYER_SUBSCRIBED_CHANNELS_LIST_KEY.get(Config.get()));

            // lpush all items
            jedis.lpush(REDIS_PLAYER_SUBSCRIBED_CHANNELS_LIST_KEY.get(Config.get()), subscribed.stream()
                    .map(Channel::getHandle)
                    .toArray(String[]::new));
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
            jedis.set(REDIS_PLAYER_CURRENT_CHANNEL_KEY.get(Config.get()), channel.getHandle());
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
            return jedis.lrange(REDIS_PLAYER_SUBSCRIBED_CHANNELS_LIST_KEY.get(Config.get()), 0, -1).stream()
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
            return SpaceChat.getInstance().getChannelManager().get(jedis.get(REDIS_PLAYER_CURRENT_CHANNEL_KEY.get(Config.get())), null);
        }
    }
}
