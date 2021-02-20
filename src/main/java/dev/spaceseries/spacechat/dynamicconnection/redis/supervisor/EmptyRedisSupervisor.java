package dev.spaceseries.spacechat.dynamicconnection.redis.supervisor;

import dev.spaceseries.spacechat.dynamicconnection.redis.RedisChatMessage;

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
    public void publishChatMessage(RedisChatMessage redisChatMessage) {
    }
}
