package dev.spaceseries.spacechat.sync;

import dev.spaceseries.spacechat.sync.packet.ReceiveStreamDataPacket;
import dev.spaceseries.spacechat.sync.packet.SendStreamDataPacket;

public abstract class ServerStreamSyncService extends ServerSyncService {

    /**
     * Construct server sync service
     *
     * @param serviceManager service manager
     */
    public ServerStreamSyncService(ServerSyncServiceManager serviceManager) {
        super(serviceManager);
    }

    /**
     * Publishes a chat message across the server
     */
    public abstract void publishChat(SendStreamDataPacket<?> packet);

    /**
     * Publishes a broadcast message across the server
     */
    public abstract void publishBroadcast(SendStreamDataPacket<?> packet);

    /**
     * Receives an incoming chat message
     */
    public abstract void receiveChat(ReceiveStreamDataPacket<?> packet);

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
