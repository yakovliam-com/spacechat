package dev.spaceseries.spacechat.sync.memory.data;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.model.manager.MapManager;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.sync.ServerDataSyncService;
import dev.spaceseries.spacechat.sync.ServerSyncServiceManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

        SpaceChat.getInstance().getServer().getPluginManager().registerEvents(playerSubscribedChannelManager, SpaceChat.getInstance());
        SpaceChat.getInstance().getServer().getPluginManager().registerEvents(playerCurrentChannelManager, SpaceChat.getInstance());
    }

    /**
     * Subscribes a player to a channel
     *
     * @param uuid    uuid
     * @param channel channel
     */
    @Override
    public void subscribeToChannel(UUID uuid, Channel channel) {
        List<Channel> subscribed = this.playerSubscribedChannelManager.get(uuid, new ArrayList<>());
        subscribed.add(channel);

        this.playerSubscribedChannelManager.add(uuid, subscribed);
    }

    /**
     * Unsubscribes a player from a channel
     *
     * @param uuid              uuid
     * @param subscribedChannel subscribed channel
     */
    @Override
    public void unsubscribeFromChannel(UUID uuid, Channel subscribedChannel) {
        List<Channel> subscribed = this.playerSubscribedChannelManager.get(uuid, new ArrayList<>());
        subscribed.removeIf(c -> c.getHandle().equals(subscribedChannel.getHandle()));
        // this.playerSubscribedChannelManager.add(uuid, subscribed);
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
        Channel channel = this.playerCurrentChannelManager.get(uuid, null);

        return channel == null ? null : SpaceChat.getInstance().getChannelManager().get(channel.getHandle(), null);
    }

    /**
     * Gets a list of all of the uuids of players who are currently subscribed to a given channel
     * <p>
     * This only includes the uuids of players who are currently online
     *
     * @param channel channel
     * @return uuids
     */
    @Override
    public List<UUID> getSubscribedUUIDs(Channel channel) {
        return this.playerSubscribedChannelManager.getAll()
                .entrySet()
                .stream()
                .filter((e -> e.getValue()
                        .stream().anyMatch(c -> c.getHandle().equals(channel.getHandle()))))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static class PlayerSubscribedChannelManager extends MapManager<UUID, List<Channel>> implements Listener {
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            this.remove(event.getPlayer().getUniqueId());
        }
    }

    private static class PlayerCurrentChannelManager extends MapManager<UUID, Channel> implements Listener {
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            this.remove(event.getPlayer().getUniqueId());
        }
    }
}
