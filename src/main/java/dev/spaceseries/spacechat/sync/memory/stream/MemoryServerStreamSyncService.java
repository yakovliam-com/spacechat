package dev.spaceseries.spacechat.sync.memory.stream;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.sync.ServerStreamSyncService;
import dev.spaceseries.spacechat.sync.ServerSyncServiceManager;
import dev.spaceseries.spacechat.sync.packet.ReceiveStreamDataPacket;
import dev.spaceseries.spacechat.sync.packet.SendStreamDataPacket;

import java.util.List;
import java.util.Set;

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
     * Publishes a player mesage across the server
     *
     * @param packet packet
     */
    @Override
    public void publishMessage(SendStreamDataPacket<?> packet) {

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
     * Publishes a online players across the server
     *
     * @param players players
     */
    @Override
    public void publishPlayerList(Set<String> players) {

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
     * Receives an incoming player message
     *
     * @param packet packet
     */
    @Override
    public void receiveMessage(ReceiveStreamDataPacket<?> packet) {

    }

    /**
     * Receives an incoming player list
     *
     * @param packet packet
     */
    @Override
    public void receivePlayerList(ReceiveStreamDataPacket<?> packet) {

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
