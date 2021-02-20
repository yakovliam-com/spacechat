package dev.spaceseries.spacechat.dynamicconnection.redis;

import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;

import java.util.UUID;

public class RedisChatMessage {

    /**
     * Who the message was sent by
     */
    private UUID sender;

    /**
     * Sender name
     */
    private String senderName;

    /**
     * The actual chat message as a component
     */
    private Component component;

    /**
     * Construct redis chat message
     */
    public RedisChatMessage(UUID sender, String senderName, Component component) {
        this.sender = sender;
        this.senderName = senderName;
        this.component = component;
    }

    /**
     * Construct redis chat message
     */
    public RedisChatMessage() {
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
     * @return sender name
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Set sender name
     * @param senderName sender name
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
