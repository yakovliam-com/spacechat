package dev.spaceseries.spacechat.dc.redis.packet.chat;

import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spacechat.dc.redis.packet.PacketType;
import dev.spaceseries.spacechat.dc.redis.packet.RedisPacket;

import java.util.UUID;

public class RedisChatPacket extends RedisPacket {

    /**
     * Who the message was sent by
     */
    private UUID sender;

    /**
     * Sender name
     */
    private String senderName;

    /**
     * The identifier of the server that the chat message is from
     */
    private String serverIdentifier;

    /**
     * The display name  of the server that the chat message is from
     */
    private String serverDisplayName;

    /**
     * The actual chat message as a component
     */
    private Component component;

    /**
     * Construct redis chat message
     */
    public RedisChatPacket(UUID sender, String senderName, String serverIdentifier, String serverDisplayName, Component component) {
        this();
        this.sender = sender;
        this.senderName = senderName;
        this.serverIdentifier = serverIdentifier;
        this.serverDisplayName = serverDisplayName;
        this.component = component;
    }

    /**
     * Construct redis chat message
     */
    public RedisChatPacket() {
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
     * Gets component
     *
     * @return component
     */
    public Component getComponent() {
        return component;
    }

    /**
     * Sets component
     *
     * @param component component
     */
    public void setComponent(Component component) {
        this.component = component;
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
     * Set sender name
     *
     * @param senderName sender name
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
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
