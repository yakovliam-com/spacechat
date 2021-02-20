package dev.spaceseries.spacechat.dc.redis.supervisor;

import dev.spaceseries.spacechat.dc.redis.RedisChatMessage;

public class EmptyRedisSupervisor extends RedisSupervisor {

    /**
     * Construct supervisor
     **/
    public EmptyRedisSupervisor() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public void publishChatMessage(RedisChatMessage redisChatMessage) {
    }
}
