package dev.spaceseries.spacechat.model;

import dev.spaceseries.spacechat.SpaceChatPlugin;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class User {

    /**
     * UUID
     */
    private final UUID uuid;

    private final List<String> ignoredUsers;
    /**
     * Username
     */
    private final String username;

    /**
     * Time/Instant that the user was created / joined for the first time
     */
    private final Date date;

    /**
     * Current channels list
     */
    private Channel currentChannel;

    /**
     * Subscribed channels list
     */
    private final List<Channel> subscribedChannels;

    /**
     * Plugin context
     */
    private final SpaceChatPlugin plugin;

    /**
     * @param uuid     uuid
     * @param username username
     * @param date     date
     */
    public User(SpaceChatPlugin plugin, UUID uuid, String username, Date date, List<Channel> subscribedChannels) {
        this.plugin = plugin;
        this.username = username;
        this.uuid = uuid;
        this.date = date;
        this.currentChannel = null;
        this.subscribedChannels = subscribedChannels;

        // on initialization, subscribe to stored subscribed list (parameter in constructor)
        // aka get from storage and also save to storage when a player calls one of the below methods about channel
        // management.
        List<Channel> serverSideSubscribedList = plugin.getServerSyncServiceManager().getDataService().getSubscribedChannels(uuid);

        List<Channel> toUnsubscribe = serverSideSubscribedList.stream()
                .filter(element -> !subscribedChannels.contains(element))
                .collect(Collectors.toList());

        toUnsubscribe.forEach(u -> plugin.getServerSyncServiceManager().getDataService().unsubscribeFromChannel(uuid, u));

        List<Channel> toSubscribe = subscribedChannels.stream()
                .filter(element -> !serverSideSubscribedList.contains(element))
                .collect(Collectors.toList());

        toSubscribe.forEach(u -> plugin.getServerSyncServiceManager().getDataService().subscribeToChannel(uuid, u));

        ignoredUsers = plugin.getStorageManager().getCurrent().getIgnoreList(username);
    }

    /**
     * Returns username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns ignored users
     *
     * @return ignored users
     */
    public List<String> getIgnoredUsers() {
        return ignoredUsers;
    }


    public boolean isIgnored(String ignoredUsername){
        return plugin.getStorageManager().getCurrent().isIgnored(username, ignoredUsername);
    }
    /**
     * Returns uuid
     *
     * @return uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns date
     *
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the current channel
     *
     * @return channel
     */
    public Channel getCurrentChannel() {
        return currentChannel;
    }

    /**
     * Returns the currently subscribed channels list
     *
     * @return channels
     */
    public List<Channel> getSubscribedChannels() {
        return subscribedChannels;
    }

    /**
     * Joins a channel
     *
     * @param channel channel
     */
    public void joinChannel(Channel channel) {
        plugin.getServerSyncServiceManager().getDataService().updateCurrentChannel(uuid, channel);

        this.currentChannel = channel;
    }

    /**
     * Leaves a channel
     * <p>
     * The channel param doesn't do anything at the moment, but it may be responsible for data management
     * in the future
     *
     * @param channel channel, optional
     */
    public void leaveChannel(Channel channel) {
        plugin.getServerSyncServiceManager().getDataService().updateCurrentChannel(uuid, null);

        this.currentChannel = null;
    }

    /**
     * Subscribe to a channel
     *
     * @param channel channel
     */
    public void subscribeToChannel(Channel channel) {
        // subscribe to channel
        plugin.getServerSyncServiceManager().getDataService().subscribeToChannel(uuid, channel);

        // add to subscribed channels list (if not exists)
        if (this.subscribedChannels.stream()
                .anyMatch(c -> c.getHandle().equals(channel.getHandle()))) {
            return;
        }
        this.subscribedChannels.add(channel);
    }

    /**
     * Unsubscribes from a channel
     *
     * @param channel channel
     */
    public void unsubscribeFromChannel(Channel channel) {
        // unsubscribe from channel
        plugin.getServerSyncServiceManager().getDataService().unsubscribeFromChannel(uuid, channel);

        // remove from subscribed channels list (in this obj)
        this.subscribedChannels.removeIf(c -> channel.getHandle().equals(c.getHandle()));
    }
}
