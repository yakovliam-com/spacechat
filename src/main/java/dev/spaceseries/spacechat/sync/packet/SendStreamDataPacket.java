package dev.spaceseries.spacechat.sync.packet;

/**
 * Allows for the splitting of sent and received data packets (based on the generic medium that is used
 * for communication)
 *
 * @param <T> The data type
 */
public interface SendStreamDataPacket<T> extends StreamDataPacket {
}
