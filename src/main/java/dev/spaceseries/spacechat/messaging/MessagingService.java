package dev.spaceseries.spacechat.messaging;

import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.messaging.redis.supervisor.RedisSupervisor;

import static dev.spaceseries.spacechat.config.Config.REDIS_ENABLED;

public class MessagingService extends InternalMessagingService<MessengerSupervisor> {

    /**
     * Construct messaging service
     */
    public MessagingService() {
        // redis
        // is redis enabled?
        if (REDIS_ENABLED.get(Config.get())) {
            // instantiate and initialize supervisor
            this.supervisor = new RedisSupervisor();
        } else {
            // else nothing...no need for a supervisor or any kind of dynamic connection
            this.supervisor = new EmptyMessengerSupervisor();
        }

        // initialize supervisor
        this.supervisor.initialize();
    }
}
