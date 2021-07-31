package dev.spaceseries.spacechat.sync;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.Channel;

import java.util.List;
import java.util.UUID;

public abstract class ServerDataSyncService extends ServerSyncService {

    /**
     * Construct server sync service
     *
     * @param serviceManager service manager
     */
    public ServerDataSyncService(SpaceChatPlugin plugin, ServerSyncServiceManager serviceManager) {
        super(plugin, serviceManager);
    }

    /**
     * Subscribes a player to a channel
     *
     * @param uuid    uuid
     * @param channel channel
     */
    public abstract void subscribeToChannel(UUID uuid, Channel channel);

    /**
     * Unsubscribes a player from a channel
     *
     * @param uuid              uuid
     * @param subscribedChannel subscribed channel
     */
    public abstract void unsubscribeFromChannel(UUID uuid, Channel subscribedChannel);

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

    /**
     * Gets a list of all of the uuids of players who are currently subscribed to a given channel
     * <p>
     * This only includes the uuids of players who are currently online
     *
     * @param channel channel
     * @return uuids
     */
    public abstract List<UUID> getSubscribedUUIDs(Channel channel);
}
