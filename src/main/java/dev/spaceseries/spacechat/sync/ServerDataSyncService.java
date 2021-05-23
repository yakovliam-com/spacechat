package dev.spaceseries.spacechat.sync;

import dev.spaceseries.spacechat.model.Channel;

import java.util.List;
import java.util.UUID;

public abstract class ServerDataSyncService extends ServerSyncService {

    /**
     * Construct server sync service
     *
     * @param serviceManager service manager
     */
    public ServerDataSyncService(ServerSyncServiceManager serviceManager) {
        super(serviceManager);
    }

    /**
     * Updates the list of subscribed channels
     *
     * @param uuid       uuid
     * @param subscribed subscribed
     */
    public abstract void updateSubscribedChannels(UUID uuid, List<Channel> subscribed);

    /**
     * Updates the current channel that a player is talking in
     *
     * @param uuid    uuid
     * @param channel channel
     */
    public abstract void updateCurrentChannel(UUID uuid, Channel channel);

    /**
     * Gets a list of subscribed channels
     *
     * @param uuid uuid
     * @return channels
     */
    public abstract List<Channel> getSubscribedChannels(UUID uuid);

    /**
     * Gets the current channel that a player is talking in
     *
     * @param uuid uuid
     * @return channel
     */
    public abstract Channel getCurrentChannel(UUID uuid);

}
