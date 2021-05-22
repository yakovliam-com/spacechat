package dev.spaceseries.spacechat.messaging;

import dev.spaceseries.spacechat.messaging.packet.receive.ReceiveMessageDataPacket;
import dev.spaceseries.spacechat.messaging.packet.send.SendMessageDataPacket;

public abstract class MessageHandlerSupervisor {

    /**
     * Supervised
     */
    protected SupervisedMessageHandler supervised;

    /**
     * Construct supervisor
     *
     * @param supervised supervised
     */
    public MessageHandlerSupervisor(SupervisedMessageHandler supervised) {
        this.supervised = supervised;
    }

    /**
     * Construct supervisor
     */
    public MessageHandlerSupervisor() {
    }

    /**
     * Returns supervised
     *
     * @return supervised
     */
    public SupervisedMessageHandler getSupervised() {
        return supervised;
    }

    /**
     * Initialize
     */
    public abstract void initialize();

    /**
     * Publish chat message
     */
    public abstract void publishChatMessage(SendMessageDataPacket<?> data);

    /**
     * Publish broadcast
     */
    public abstract void publishBroadcast(SendMessageDataPacket<?> data);

    /**
     * Receive chat message
     */
    public abstract void receiveChatMessage(ReceiveMessageDataPacket<?> data);

    /**
     * Receive broadcast
     */
    public abstract void receiveBroadcast(ReceiveMessageDataPacket<?> data);

    /**
     * Stops a supervised task from running
     */
    public abstract void stop();
}
