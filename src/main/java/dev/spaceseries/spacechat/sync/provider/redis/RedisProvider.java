package dev.spaceseries.spacechat.sync.provider.redis;

import dev.spaceseries.spaceapi.lib.redis.jedis.JedisPool;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.SpaceChatConfig;
import dev.spaceseries.spacechat.config.SpaceChatConfigKeys;
import dev.spaceseries.spacechat.sync.provider.Provider;

import java.net.URI;
import java.net.URISyntaxException;

public class RedisProvider implements Provider<JedisPool> {

    /**
     * Pool
     */
    private JedisPool pool;

    /**
     * Construct redis provider
     */
    public RedisProvider(SpaceChat plugin) {
        try {
            // initialize pool
            pool = new JedisPool(new URI(SpaceChatConfigKeys.REDIS_URL.get(plugin.getSpaceChatConfig().getAdapter())));
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
