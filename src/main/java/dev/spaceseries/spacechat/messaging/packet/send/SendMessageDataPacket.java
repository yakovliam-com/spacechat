package dev.spaceseries.spacechat.messaging.packet.send;

import dev.spaceseries.spacechat.messaging.packet.MessageDataPacket;

/**
 * Allows for the splitting of sent and received data packets (based on the generic medium that is used
 * for communication)
 *
 * @param <T> The data type
 */
public interface SendMessageDataPacket<T> extends MessageDataPacket {
}
