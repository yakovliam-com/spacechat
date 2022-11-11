package dev.spaceseries.spacechat.sync.redis.stream.packet.message;

import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.sync.packet.SendStreamDataPacket;
import dev.spaceseries.spacechat.sync.redis.stream.packet.PacketType;
import dev.spaceseries.spacechat.sync.redis.stream.packet.RedisPacket;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class RedisMessagePacket extends RedisPacket implements SendStreamDataPacket<Void> {

    /**
     * Who the message was sent by
     */
    private UUID sender;

    /**
     * Sender name
     */
    private String senderName;

    /**
     * Receiver name
     */
    private String receiverName;

    /**
     * The chat channel that the user is currently in
     */
    private Channel channel;

    /**
     * The identifier of the server that the chat message is from
     */
    private String serverIdentifier;

    /**
     * The display name  of the server that the chat message is from
     */
    private String serverDisplayName;

    /**
     * The actual chat message
     */
    private String message;

    /**
     * Construct redis chat message
     */
    public RedisMessagePacket(UUID sender, String senderName, String receiverName, Channel channel, String serverIdentifier, String serverDisplayName, String message) {
        this();
        this.sender = sender;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.channel = channel;
        this.serverIdentifier = serverIdentifier;
        this.serverDisplayName = serverDisplayName;
        this.message = message;
    }

    /**
     * Construct redis chat message
     */
    public RedisMessagePacket() {
        super(PacketType.CHAT);
    }

    /**
     * Gets sender
     *
     * @return sender
     */
    public UUID getSender() {
        return sender;
    }

    /**
     * Sets sender
     *
     * @param sender sender
     */
    public void setSender(UUID sender) {
        this.sender = sender;
    }

    /**
     * Gets messag
     *
     * @return text message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message
     *
     * @param message text message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get sender name
     *
     * @return sender name
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Get receiver name
     */
    public String getReceiverName(){
        return receiverName;
    }

    /**
     * Set sender name
     *
     * @param senderName sender name
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * Returns channel
     *
     * @return channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Sets channel
     *
     * @param channel channel
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * Get the name of the server that the packet is from
     *
     * @return server name
     */
    public String getServerIdentifier() {
        return serverIdentifier;
    }

    /**
     * Set the name of the server that the packet is from
     *
     * @param serverIdentifier server name
     */
    public void setServerIdentifier(String serverIdentifier) {
        this.serverIdentifier = serverIdentifier;
    }

    /**
     * Get the server display name
     *
     * @return display name
     */
    public String getServerDisplayName() {
        return serverDisplayName;
    }

    /**
     * Set the server display name
     *
     * @param serverDisplayName display name
     */
    public void setServerDisplayName(String serverDisplayName) {
        this.serverDisplayName = serverDisplayName;
    }
}
