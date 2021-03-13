package dev.spaceseries.spacechat.dc.redis.supervisor;

import dev.spaceseries.spacechat.dc.redis.packet.broadcast.RedisBroadcastPacket;
import dev.spaceseries.spacechat.dc.redis.packet.chat.RedisChatPacket;

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
    public void publishChatMessage(RedisChatPacket redisChatMessage) {
    }

    @Override
    public void publishBroadcast(RedisBroadcastPacket redisBroadcastPacket) {
    }
}
