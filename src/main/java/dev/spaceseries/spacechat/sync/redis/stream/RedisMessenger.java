package dev.spaceseries.spacechat.sync.redis.stream;

import dev.spaceseries.spaceapi.lib.redis.jedis.Jedis;
import dev.spaceseries.spaceapi.lib.redis.jedis.JedisPool;
import dev.spaceseries.spaceapi.lib.redis.jedis.JedisPubSub;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.sync.packet.StreamDataPacket;
import dev.spaceseries.spacechat.sync.redis.stream.packet.RedisPublishDataPacket;
import dev.spaceseries.spacechat.sync.redis.stream.packet.RedisStringReceiveDataPacket;
import org.bukkit.Bukkit;

import java.util.logging.Level;

import static dev.spaceseries.spacechat.config.SpaceChatConfigKeys.*;

public class RedisMessenger extends JedisPubSub {

    /**
     * Sync service
     */
    private final RedisServerStreamSyncService syncService;

    /**
     * Pool
     */
    private final JedisPool pool;

    /**
     * Plugin
     */
    private final SpaceChat plugin;

    /**
     * Construct redis connector
     */
    public RedisMessenger(SpaceChat plugin, RedisServerStreamSyncService syncService) {
        this.plugin = plugin;
        this.syncService = syncService;

        // initialize pool
        this.pool = syncService.getServiceManager().getRedisProvider().provide();

        // subscribing to redis pub/sub is a blocking operation.
        // we need to make a new thread in order to not block the main thread....
        new Thread(() -> {
            try (Jedis jedis = pool.getResource()) {
                // subscribe this class to chat channel
                jedis.subscribe(this, REDIS_CHAT_CHANNEL.get(this.plugin.getSpaceChatConfig().getAdapter()));
            }
        }).start();

        // create a separate thread for broadcast packets
        new Thread(() -> {
            try (Jedis jedis = pool.getResource()) {
                // subscribe this class to chat channel
                jedis.subscribe(this, REDIS_BROADCAST_CHANNEL.get(this.plugin.getSpaceChatConfig().getAdapter()));
            }
        }).start();
    }

    /**
     * Shuts down the client
     */
    public void shutdown() {
        if (this.pool != null && this.pool.getResource().getClient() != null) {
            // unsubscribe from chat channel
            unsubscribe(REDIS_CHAT_CHANNEL.get(plugin.getSpaceChatConfig().getAdapter()));
            unsubscribe(REDIS_BROADCAST_CHANNEL.get(plugin.getSpaceChatConfig().getAdapter()));

            pool.close();
        }
    }

    @Override
    public void onMessage(String channel, String message) {
        // receiving
        // [channel] sent [message]

        // if it's the correct channel
        if (channel.equalsIgnoreCase(REDIS_CHAT_CHANNEL.get(plugin.getSpaceChatConfig().getAdapter())))
            this.syncService.receiveChat(new RedisStringReceiveDataPacket(message));
        else if (channel.equalsIgnoreCase(REDIS_BROADCAST_CHANNEL.get(plugin.getSpaceChatConfig().getAdapter())))
            this.syncService.receiveBroadcast(new RedisStringReceiveDataPacket(message));
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        // we have subscribed to [channel]. We are currently subscribed to [subscribedChannels] channels.
        plugin.getLogger().log(Level.INFO, "SpaceChat subscribed to the redis channel '" + channel + "'");
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        // we have unsubscribed from [channel]. We are currently subscribed to another [subscribedChannels] channels.
        plugin.getLogger().log(Level.INFO, "SpaceChat unsubscribed from the redis channel '" + channel + "'");
    }

    /**
     * Publish a message
     *
     * @param dataPacket packet
     */
    public void publish(StreamDataPacket dataPacket) {
        RedisPublishDataPacket redisPublishDataPacket = (RedisPublishDataPacket) dataPacket;

        String channel = redisPublishDataPacket.getChannel();
        String message = redisPublishDataPacket.getMessage();
        // run async
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.publish(channel, message);
            }
        });
    }
}
