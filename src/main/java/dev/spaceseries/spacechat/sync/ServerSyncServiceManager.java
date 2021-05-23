package dev.spaceseries.spacechat.sync;

import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.sync.memory.data.MemoryServerDataSyncService;
import dev.spaceseries.spacechat.sync.memory.stream.MemoryServerStreamSyncService;
import dev.spaceseries.spacechat.sync.provider.redis.RedisProvider;
import dev.spaceseries.spacechat.sync.redis.data.RedisServerDataSyncService;
import dev.spaceseries.spacechat.sync.redis.stream.RedisServerStreamSyncService;
import org.jetbrains.annotations.Nullable;

import static dev.spaceseries.spacechat.config.Config.REDIS_ENABLED;

public class ServerSyncServiceManager {

    /**
     * Is the sync service using the network?
     * <p>
     * E.g. is it using Redis or Not?
     */
    private final boolean usingNetwork;

    /**
     * The service responsible for the stream of data like chat messages and broadcasts
     */
    private final ServerStreamSyncService streamService;

    /**
     * The service responsible for the data transferring like channel updates
     */
    private final ServerDataSyncService dataService;

    /**
     * Redis pool provider
     * <p>
     * TODO in the future, I want to modularize / abstract-ize this concept
     */
    private RedisProvider redisProvider;

    /**
     * Construct server sync service manager
     */
    public ServerSyncServiceManager() {
        // if redis is enabled, use that
        if (REDIS_ENABLED.get(Config.get())) {
            // initialize redis services
            this.redisProvider = new RedisProvider();

            // initialize services
            this.streamService = new RedisServerStreamSyncService(this);
            this.dataService = new RedisServerDataSyncService(this);

            this.usingNetwork = true;
        } else {
            // since we're not using redis, just implement the memory version of the service

            // initialize services
            this.streamService = new MemoryServerStreamSyncService(this);
            this.dataService = new MemoryServerDataSyncService(this);

            this.usingNetwork = false;
        }

        // call the initialization method for the services
        streamService.start();
    }

    /**
     * Returns true if the sync service using the network
     * <p>
     * E.g. is it using Redis or Not?
     *
     * @return is using network
     */
    public boolean isUsingNetwork() {
        return usingNetwork;
    }

    /**
     * Returns the redis provider
     * <p>
     * Null if not implemented
     *
     * @return redis provider
     */
    public @Nullable RedisProvider getRedisProvider() {
        return redisProvider;
    }

    /**
     * Returns stream service
     *
     * @return stream service
     */
    public ServerStreamSyncService getStreamService() {
        return streamService;
    }

    /**
     * Returns data service service
     *
     * @return data service
     */
    public ServerDataSyncService getDataService() {
        return dataService;
    }

    /**
     * End the service
     * <p>
     * Mostly for connections to databases or messaging agents
     */
    public void end() {
        this.streamService.end();

        if (this.redisProvider != null) {
            this.redisProvider.end();
        }
    }
}
