package dev.spaceseries.spacechat.sync.memory.data;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.manager.MapManager;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.sync.ServerDataSyncService;
import dev.spaceseries.spacechat.sync.ServerSyncServiceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryServerDataSyncService extends ServerDataSyncService {

    /**
     * Player subscribed channel manager
     */
    private final PlayerSubscribedChannelManager playerSubscribedChannelManager;

    /**
     * Player current channel manager
     */
    private final PlayerCurrentChannelManager playerCurrentChannelManager;

    /**
     * Construct server sync service
     *
     * @param serviceManager service manager
     */
    public MemoryServerDataSyncService(ServerSyncServiceManager serviceManager) {
        super(serviceManager);
        this.playerSubscribedChannelManager = new PlayerSubscribedChannelManager();
        this.playerCurrentChannelManager = new PlayerCurrentChannelManager();
    }

    /**
     * Updates the list of subscribed channels
     *
     * @param uuid       uuid
     * @param subscribed subscribed
     */
    @Override
    public void updateSubscribedChannels(UUID uuid, List<Channel> subscribed) {
        this.playerSubscribedChannelManager.add(uuid, subscribed);
    }

    /**
     * Updates the current channel that a player is talking in
     *
     * @param uuid    uuid
     * @param channel channel
     */
    @Override
    public void updateCurrentChannel(UUID uuid, Channel channel) {
        this.playerCurrentChannelManager.add(uuid, channel);
    }

    /**
     * Gets a list of subscribed channels
     *
     * @param uuid uuid
     * @return channels
     */
    @Override
    public List<Channel> getSubscribedChannels(UUID uuid) {
        return this.playerSubscribedChannelManager.get(uuid, new ArrayList<>());
    }

    /**
     * Gets the current channel that a player is talking in
     *
     * @param uuid uuid
     * @return channel
     */
    @Override
    public Channel getCurrentChannel(UUID uuid) {
        // the reason I pull from the connection manager instead of just using the provided channel object
        // in memory is because it's possible that the channels won't line up if there is POSSIBLY a network connection
        // still going on AND the configurations are different between servers. It's VERY unlikely, but I still like to
        // account for every situation.
        return SpaceChat.getInstance().getChannelManager().get(this.playerCurrentChannelManager.get(uuid).getHandle(), null);
    }

    private static class PlayerSubscribedChannelManager extends MapManager<UUID, List<Channel>> {
    }

    private static class PlayerCurrentChannelManager extends MapManager<UUID, Channel> {
    }
}
