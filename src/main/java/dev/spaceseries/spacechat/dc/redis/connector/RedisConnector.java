package dev.spaceseries.spacechat.dc.redis.connector;

import dev.spaceseries.spaceapi.lib.redis.jedis.Jedis;
import dev.spaceseries.spaceapi.lib.redis.jedis.JedisPool;
import dev.spaceseries.spaceapi.lib.redis.jedis.JedisPubSub;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.dc.redis.supervisor.RedisSupervisor;
import org.bukkit.Bukkit;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import static dev.spaceseries.spacechat.configuration.Config.*;

public class RedisConnector extends JedisPubSub {

    /**
     * Redis client
     */
    private JedisPool pool;

    /**
     * The supervisor
     */
    private final RedisSupervisor supervisor;

    /**
     * Construct redis connector
     */
    public RedisConnector(RedisSupervisor supervisor) {
        this.supervisor = supervisor;

        try {
            pool = new JedisPool(new URI(REDIS_URL.get(Config.get())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            pool = null;
            return;
        }

        // subscribing to redis pub/sub is a blocking operation.
        // we need to make a new thread in order to not block the main thread....
        new Thread(() -> {
            try (Jedis jedis = pool.getResource()) {
                // subscribe this class to chat channel
                jedis.subscribe(this, REDIS_CHAT_CHANNEL.get(Config.get()));
            }
        }).start();

        // create a separate thread for broadcast packets
        new Thread(() -> {
            try (Jedis jedis = pool.getResource()) {
                // subscribe this class to chat channel
                jedis.subscribe(this, REDIS_BROADCAST_CHANNEL.get(Config.get()));
            }
        }).start();
    }

    /**
     * Shuts down the client
     */
    public void shutdown() {
        // unsubscribe from chat channel
        unsubscribe(REDIS_CHAT_CHANNEL.get(Config.get()));
        unsubscribe(REDIS_BROADCAST_CHANNEL.get(Config.get()));
        pool.close();
    }

    @Override
    public void onMessage(String channel, String message) {
        // receiving
        // [channel] sent [message]

        // if it's the correct channel
        if (channel.equalsIgnoreCase(REDIS_CHAT_CHANNEL.get(Config.get())))
            this.supervisor.receiveChatMessage(message);
        else if (channel.equalsIgnoreCase(REDIS_BROADCAST_CHANNEL.get(Config.get())))
            this.supervisor.receiveBroadcast(message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        // we have subscribed to [channel]. We are currently subscribed to [subscribedChannels] channels.
        //TODO logging in console
        SpaceChat.getInstance().getLogger().log(Level.INFO, "SpaceChat subscribed to the redis channel '" + channel + "'");
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        // we have unsubscribed from [channel]. We are currently subscribed to another [subscribedChannels] channels.
        //TODO logging in console
        SpaceChat.getInstance().getLogger().log(Level.INFO, "SpaceChat unsubscribed from the redis channel '" + channel + "'");
    }

    /**
     * Publish a message
     *
     * @param channel channel
     * @param message message
     */
    public void publish(String channel, String message) {
        // run async
        Bukkit.getScheduler().runTaskAsynchronously(SpaceChat.getInstance(), () -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.publish(channel, message);
            }
        });
    }
}
