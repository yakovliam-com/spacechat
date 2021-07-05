package dev.spaceseries.spacechat.sync.redis.stream.packet;

import dev.spaceseries.spacechat.sync.packet.ReceiveStreamDataPacket;

public class RedisStringReceiveDataPacket implements ReceiveStreamDataPacket<String> {

    /**
     * Data
     */
    private final String data;

    /**
     * Construct data packet
     *
     * @param data data
     */
    public RedisStringReceiveDataPacket(String data) {
        this.data = data;
    }

    /**
     * Returns data
     *
     * @return data
     */
    public String getData() {
        return data;
    }
}
