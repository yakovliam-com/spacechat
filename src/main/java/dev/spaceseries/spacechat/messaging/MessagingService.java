package dev.spaceseries.spacechat.messaging;

import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.messaging.empty.EmptyMessengerSupervisor;
import dev.spaceseries.spacechat.messaging.redis.RedisSupervisor;

import static dev.spaceseries.spacechat.config.Config.REDIS_ENABLED;

public class MessagingService extends InternalMessagingService<MessageHandlerSupervisor> {

    /**
     * Is it actually implemented?
     */
    private final boolean isImplemented;

    /**
     * Construct messaging service
     */
    public MessagingService() {
        // redis
        // is redis enabled?
        if (REDIS_ENABLED.get(Config.get())) {
            // instantiate and initialize supervisor
            this.supervisor = new RedisSupervisor();
            this.isImplemented = true;
        } else {
            // else nothing...no need for a supervisor or any kind of dynamic connection
            this.supervisor = new EmptyMessengerSupervisor();
            this.isImplemented = false;
        }

        // initialize supervisor
        this.supervisor.initialize();
    }

    /**
     * Returns if the service is actually implemented
     *
     * @return is implemented?
     */
    public boolean isImplemented() {
        return isImplemented;
    }
}
