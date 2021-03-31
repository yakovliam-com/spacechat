package dev.spaceseries.spacechat.messaging.packet.receive;

import dev.spaceseries.spacechat.messaging.packet.MessageDataPacket;

/**
 * Allows for the splitting of sent and received data packets (based on the generic medium that is used
 * for communication)
 *
 * @param <T> The data type
 */
public interface ReceiveMessageDataPacket<T> extends MessageDataPacket {
}
