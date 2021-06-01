package dev.spaceseries.spacechat.model;

import dev.spaceseries.spacechat.SpaceChat;

import java.util.Date;
import java.util.UUID;

public final class User {

    /**
     * UUID
     */
    private final UUID uuid;

    /**
     * Username
     */
    private final String username;

    /**
     * Time/Instant that the user was created / joined for the first time
     */
    private final Date date;

    /**
     * @param uuid     uuid
     * @param username username
     * @param date     date
     */
    public User(UUID uuid, String username, Date date) {
        this.username = username;
        this.uuid = uuid;
        this.date = date;

        // TODO on initialization, subscribe to stored subscribed list (parameter in constructor)
        // aka get from storage and also save to storage when a player calls one of the below methods about channel
        // management.
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
     * Joins a channel
     *
     * @param channel channel
     */
    public void joinChannel(Channel channel) {
        SpaceChat.getInstance().getServerSyncServiceManager().getDataService().updateCurrentChannel(uuid, channel);
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
        SpaceChat.getInstance().getServerSyncServiceManager().getDataService().updateCurrentChannel(uuid, null);
    }

    /**
     * Subscribe to a channel
     *
     * @param channel channel
     */
    public void subscribeToChannel(Channel channel) {
        // subscribe to channel
        SpaceChat.getInstance().getServerSyncServiceManager().getDataService().subscribeToChannel(uuid, channel);
    }

    /**
     * Unsubscribes from a channel
     *
     * @param channel channel
     */
    public void unsubscribeFromChannel(Channel channel) {
        // unsubscribe from channel
        SpaceChat.getInstance().getServerSyncServiceManager().getDataService().unsubscribeFromChannel(uuid, channel);
    }
}
