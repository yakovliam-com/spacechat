package dev.spaceseries.spacechat.sync;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.sync.packet.ReceiveStreamDataPacket;
import dev.spaceseries.spacechat.sync.packet.SendStreamDataPacket;

import java.util.Set;

public abstract class ServerStreamSyncService extends ServerSyncService {

    /**
     * Construct server sync service
     *
     * @param plugin         plugin
     * @param serviceManager service manager
     */
    public ServerStreamSyncService(SpaceChatPlugin plugin, ServerSyncServiceManager serviceManager) {
        super(plugin, serviceManager);
    }

    /**
     * Publishes a chat message across the server
     */
    public abstract void publishChat(SendStreamDataPacket<?> packet);

    /**
     * Publishes a player message across the server
     */
    public abstract void publishMessage(SendStreamDataPacket<?> packet);
    /**
     * Publishes a broadcast message across the server
     */
    public abstract void publishBroadcast(SendStreamDataPacket<?> packet);

    /**
     * Published a list of online players across the server
     */
    public abstract void publishPlayerList(Set<String> players);


    /**
     * Receives an incoming chat message
     */
    public abstract void receiveChat(ReceiveStreamDataPacket<?> packet);

    /**
     * Receives an incoming player message
     */
    public abstract void receiveMessage(ReceiveStreamDataPacket<?> packet);

    /**
     * Receives an incoming player list
     */
    public abstract void receivePlayerList(ReceiveStreamDataPacket<?> packet);
    /**
     * Receives an incoming chat message
     */
    public abstract void receiveBroadcast(ReceiveStreamDataPacket<?> packet);

    /**
     * Starts the service in question
     */
    public abstract void start();

    /**
     * Ends the service in question
     */
    public abstract void end();
}
