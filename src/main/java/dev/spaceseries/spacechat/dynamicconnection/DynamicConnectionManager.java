package dev.spaceseries.spacechat.dynamicconnection;

import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.dynamicconnection.redis.supervisor.EmptyRedisSupervisor;
import dev.spaceseries.spacechat.dynamicconnection.redis.supervisor.RedisSupervisor;

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
            // initialize supervisor
            redisSupervisor = new RedisSupervisor();
        } else {
            // else nothing...no need for a supervisor or any kind of dynamic connection
            redisSupervisor = new EmptyRedisSupervisor();
        }
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
