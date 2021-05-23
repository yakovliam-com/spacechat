package dev.spaceseries.spacechat.sync.provider.redis;

import dev.spaceseries.spaceapi.lib.redis.jedis.JedisPool;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.sync.provider.Provider;

import java.net.URI;
import java.net.URISyntaxException;

import static dev.spaceseries.spacechat.config.Config.REDIS_URL;

public class RedisProvider implements Provider<JedisPool> {

    /**
     * Pool
     */
    private JedisPool pool;

    /**
     * Construct redis provider
     */
    public RedisProvider() {
        try {
            // initialize pool
            pool = new JedisPool(new URI(REDIS_URL.get(Config.get())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            pool = null;
        }
    }

    @Override
    public JedisPool provide() {
        return pool;
    }

    /**
     * Ends the provided pool
     */
    public void end() {
        this.pool.close();
    }
}
