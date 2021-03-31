package dev.spaceseries.spacechat.messaging;

import dev.spaceseries.spacechat.messaging.packet.MessageDataPacket;

public interface SupervisedMessenger {

    void shutdown();

    void publish(MessageDataPacket dataPacket);
}
