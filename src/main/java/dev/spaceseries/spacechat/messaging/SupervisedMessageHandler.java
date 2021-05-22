package dev.spaceseries.spacechat.messaging;

import dev.spaceseries.spacechat.messaging.packet.MessageDataPacket;

public interface SupervisedMessageHandler {

    void shutdown();

    void publish(MessageDataPacket dataPacket);
}
