package dev.spaceseries.spacechat.dc;

import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.dc.redis.supervisor.EmptyRedisSupervisor;
import dev.spaceseries.spacechat.dc.redis.supervisor.RedisSupervisor;

import static dev.spaceseries.spacechat.configuration.Config.REDIS_ENABLED;

public class DynamicConnectionManager {

    /**
     * Redis supervisor
     */
    private final RedisSupervisor redisSupervisor;

    /**
     * Construct dynamic connection manager
     */
    public DynamicConnectionManager() {
        // redis
        // is redis enabled?
        if (REDIS_ENABLED.get(Config.get())) {
            // instantiate and initialize supervisor
            redisSupervisor = new RedisSupervisor();
        } else {
            // else nothing...no need for a supervisor or any kind of dynamic connection
            redisSupervisor = new EmptyRedisSupervisor();
        }

        // initialize supervisor
        redisSupervisor.initialize();
    }

    /**
     * Returns redis supervisor
     *
     * @return supervisor
     */
    public RedisSupervisor getRedisSupervisor() {
        return redisSupervisor;
    }
}
