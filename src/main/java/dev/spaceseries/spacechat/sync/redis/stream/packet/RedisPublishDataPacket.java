package dev.spaceseries.spacechat.sync.redis.stream.packet;

import dev.spaceseries.spacechat.sync.packet.StreamDataPacket;

public class RedisPublishDataPacket implements StreamDataPacket {

    /**
     * Channel
     */
    private final String channel;

    /**
     * Message
     */
    private final String message;

    /**
     * Constructs packet
     *
     * @param channel channel
     * @param message message
     */
    public RedisPublishDataPacket(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    /**
     * Returns channel
     *
     * @return channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Returns message
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
