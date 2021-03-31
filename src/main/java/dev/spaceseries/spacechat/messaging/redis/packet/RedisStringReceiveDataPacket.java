package dev.spaceseries.spacechat.messaging.redis.packet;

import dev.spaceseries.spacechat.messaging.packet.receive.ReceiveMessageDataPacket;

public class RedisStringReceiveDataPacket implements ReceiveMessageDataPacket<String> {

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
