package dev.spaceseries.spacechat.messaging.redis.packet;

import dev.spaceseries.spacechat.messaging.packet.MessageDataPacket;

public class RedisPublishDataPacket implements MessageDataPacket {

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
