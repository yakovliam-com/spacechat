package dev.spaceseries.spacechat.messaging;

import dev.spaceseries.spacechat.messaging.packet.receive.ReceiveMessageDataPacket;
import dev.spaceseries.spacechat.messaging.packet.send.SendMessageDataPacket;

public class EmptyMessengerSupervisor extends MessengerSupervisor {

    /**
     * Construct supervisor
     **/
    public EmptyMessengerSupervisor() {
    }

    @Override
    public void initialize() {

    }

    @Override
    public void publishChatMessage(SendMessageDataPacket<?> data) {

    }

    @Override
    public void publishBroadcast(SendMessageDataPacket<?> data) {

    }

    @Override
    public void receiveChatMessage(ReceiveMessageDataPacket<?> data) {

    }

    @Override
    public void receiveBroadcast(ReceiveMessageDataPacket<?> data) {

    }

    @Override
    public void stop() {

    }
}
