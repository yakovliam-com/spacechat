package dev.spaceseries.spacechat.messaging.redis.packet;

public abstract class RedisPacket {

    /**
     * Type of packet
     */
    private final PacketType type;

    /**
     * Construct redis packet
     *
     * @param type type
     */
    public RedisPacket(PacketType type) {
        this.type = type;
    }

    /**
     * Returns packet type
     *
     * @return type
     */
    public PacketType getType() {
        return type;
    }
}
