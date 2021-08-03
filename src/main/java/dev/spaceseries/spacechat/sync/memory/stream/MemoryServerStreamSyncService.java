package dev.spaceseries.spacechat.sync.memory.stream;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.sync.ServerStreamSyncService;
import dev.spaceseries.spacechat.sync.ServerSyncServiceManager;
import dev.spaceseries.spacechat.sync.packet.ReceiveStreamDataPacket;
import dev.spaceseries.spacechat.sync.packet.SendStreamDataPacket;

public class MemoryServerStreamSyncService extends ServerStreamSyncService {

    /**
     * Construct server sync service
     *
     * @param serviceManager service manager
     * @param plugin         plugin
     */
    public MemoryServerStreamSyncService(SpaceChatPlugin plugin, ServerSyncServiceManager serviceManager) {
        super(plugin, serviceManager);
    }

    /**
     * Publishes a chat message across the server
     *
     * @param packet packet
     */
    @Override
    public void publishChat(SendStreamDataPacket<?> packet) {

    }

    /**
     * Publishes a broadcast message across the server
     *
     * @param packet packet
     */
    @Override
    public void publishBroadcast(SendStreamDataPacket<?> packet) {

    }

    /**
     * Receives an incoming chat message
     *
     * @param packet packet
     */
    @Override
    public void receiveChat(ReceiveStreamDataPacket<?> packet) {

    }

    /**
     * Receives an incoming chat message
     *
     * @param packet packet
     */
    @Override
    public void receiveBroadcast(ReceiveStreamDataPacket<?> packet) {

    }

    /**
     * Starts the service in question
     */
    @Override
    public void start() {

    }

    /**
     * Ends the service in question
     */
    @Override
    public void end() {

    }
}
